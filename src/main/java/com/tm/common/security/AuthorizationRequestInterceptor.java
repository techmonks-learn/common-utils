package com.tm.common.security;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tm.common.constants.Constants;
import com.tm.common.jwt.JwtUtility;
import com.tm.common.security.annotations.AllowAnonymous;
import io.jsonwebtoken.Claims;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.owasp.esapi.ESAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

@Component("Authorization")
public class AuthorizationRequestInterceptor implements HandlerInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationRequestInterceptor.class);

    private static final String REQUEST = "REQUEST";
    public static final String AUTHORIZATION = "Authorization";

    @Value("${userInfoEndpoint}")
    private String userInfoEndpoint;

    @Value("${jwtSecretKey:lUYhe38sF7Rs4aYW0xZZp}")
    private String jwtSecretKey;

    private static String getClientIpAddress(HttpServletRequest request) {
        String remoteAddr = "";
        if (Objects.nonNull(request)) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (Objects.isNull(remoteAddr) || StringUtils.isBlank(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            } else {
                remoteAddr = remoteAddr.split(",")[0];
            }
        }
        return remoteAddr;
    }

    @PostConstruct
    public void init() {
        LOGGER.info(ESAPI.encoder().encodeForHTML("AuthorizationRequestInterceptor initiated..."));
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (anonymousAccessAllowed(request, handler)) {
            setAnonymousSecurityContextToRequest();
            return true;
        }
        if (!authorizeRequest(request, response)) {
            return authorizationFailed(request, response, "Invalid Token");
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        LOGGER.trace(ESAPI.encoder().encodeForHTML("Executing the postHandle method of AuthorizationRequestInterceptor"));
        MDC.clear();
    }

    // Whitelisting Hystrix calls
    private boolean isHystrixRequest(HttpServletRequest request, HttpServletResponse response) {
        return request.getRequestURI().split("/")[1].contains("hystrix");
    }

    private boolean anonymousAccessAllowed(HttpServletRequest request, Object handler) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            if (request.getDispatcherType().name().equals(REQUEST) && (method.getDeclaringClass().isAnnotationPresent(Controller.class)
                    || method.getDeclaringClass().isAnnotationPresent(RestController.class))) {
                if (method.isAnnotationPresent(AllowAnonymous.class) || method.getDeclaringClass().isAnnotationPresent(AllowAnonymous.class)) {
                    return Boolean.TRUE;
                }
            }
        }
        return Boolean.FALSE;
    }

    private Boolean authorizationFailed(HttpServletRequest request, HttpServletResponse response, String message) throws IOException, ServletException {
        response.sendError(401, message);
        LOGGER.error(ESAPI.encoder().encodeForHTML("Request Authenticity failed"));
        return Boolean.FALSE;
    }

    private boolean authorizeRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (request.getMethod().equals(RequestMethod.OPTIONS.toString())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }
        return validateRequestAndSetContext(request, response);
    }

    private boolean validateRequestAndSetContext(HttpServletRequest request, HttpServletResponse response) {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        boolean isValid = false;
        if (StringUtils.isNotBlank(authorizationHeader)) {
            if (isValidAuthorizationType(authorizationHeader)) {
                String authToken = extractBearerToken(authorizationHeader);
                if (StringUtils.isNotBlank(authToken)) {
                    Optional<Claims> claims = validateJwtAndExtractClaims(authToken);
                    if (claims.isPresent()) {
                        String subject = claims.get().getSubject();
                        Long userId = claims.get().get("Id", Long.class);
                        Optional<UserRequestContextPrincipal> userDetails = getUserDetails(userId);
                        if (userDetails.isPresent()) {
                            setSecurityContextToRequest(userDetails.get());
                            isValid = true;
                        }
                    }
                }
            }
        }
        if (!isValid) {
            try {
                response.sendError(401, "Invalid Token");
            } catch (IOException e) {
                e.printStackTrace();
            }
            LOGGER.error(ESAPI.encoder().encodeForHTML("Invalid request"));
        }
        return isValid;
    }

    private Optional<UserRequestContextPrincipal> getUserDetails(Long userId) {
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        UserRequestContextPrincipal userRequestContextPrincipal = new UserRequestContextPrincipal();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
        ResponseEntity response = template.exchange(userInfoEndpoint + 1, HttpMethod.GET, entity, String.class);
        if (response.getStatusCodeValue() == 200) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                userRequestContextPrincipal = objectMapper.readValue(response.getBody().toString(), UserRequestContextPrincipal.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                LOGGER.error(e.getMessage(), e.toString());
            }
            return Optional.of(userRequestContextPrincipal);
        }
        return Optional.empty();
    }

    private Boolean isValidAuthorizationType(String token) {
        Boolean status = Boolean.FALSE;
        if (StringUtils.isNotBlank(token)) {
            String tokens[] = token.split(Constants.Authorization.SPACE);
            if (tokens.length == 2) {
                token = tokens[0];
                if (token.equalsIgnoreCase(Constants.Authorization.BEARER_TOKEN)) {
                    status = Boolean.TRUE;
                }
            }
        }
        return status;
    }

    private Optional<Claims> validateJwtAndExtractClaims(String jwtToken) {
        try {
            //TODO: when secret key is empty, need to extract the account id from the
            // JWT and get secret key from DB based on account Id
            Claims claims = JwtUtility.decodeJWT(jwtToken, jwtSecretKey);
            return Optional.of(claims);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex.toString());
        }
        return Optional.empty();
    }

    private String extractBearerToken(String authorizationHeaderValue) {
        if (StringUtils.isNotBlank(authorizationHeaderValue)) {
            String[] tokens = authorizationHeaderValue.split(Constants.Authorization.SPACE);
            if (tokens.length > 1) {
                return tokens[1];
            }
        }
        return StringUtils.EMPTY;
    }

    private void setSecurityContextToRequest(UserRequestContextPrincipal userSecurityContextModel) {
        if (Objects.nonNull(userSecurityContextModel)) {
            List<SimpleGrantedAuthority> updatedPermissions = fillInGrants(userSecurityContextModel);
            Authentication auth = new UsernamePasswordAuthenticationToken(userSecurityContextModel, null, updatedPermissions);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
    }

    private void setAnonymousSecurityContextToRequest() {
        UserRequestContextPrincipal userSecurityContextModel = new UserRequestContextPrincipal();
        List<SimpleGrantedAuthority> updatedPermissions = fillInGrants(userSecurityContextModel);
        Authentication auth = new UsernamePasswordAuthenticationToken(userSecurityContextModel, null, updatedPermissions);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }


    private List<SimpleGrantedAuthority> fillInGrants(UserRequestContextPrincipal userRequestContextPrincipal) {
        List<SimpleGrantedAuthority> securityGrants = new ArrayList<>();
        if (Objects.nonNull(userRequestContextPrincipal) && CollectionUtils.isNotEmpty(userRequestContextPrincipal.getPrivileges())) {
            userRequestContextPrincipal.getPrivileges().forEach(item ->
                    securityGrants.add(new SimpleGrantedAuthority(item.getCode())));
        } else {
            securityGrants.add(new SimpleGrantedAuthority("Anonymous"));
        }
        return securityGrants;
    }
}
