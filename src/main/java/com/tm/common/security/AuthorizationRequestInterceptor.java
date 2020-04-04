package com.tm.common.security;


import com.tm.common.security.annotations.AllowAnonymous;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.owasp.esapi.ESAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component("Authorization")
public class AuthorizationRequestInterceptor implements HandlerInterceptor {


    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationRequestInterceptor.class);

    private static final String REQUEST = "REQUEST";
    public static final String AUTHORIZATION = "Authorization";

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
//        if(anonymousAccessAllowed(request, handler)){
//            return true;
//        }
//        if(!authorizeRequest(request, response)){
//            return authorizationFailed(request, response, "Invalid Token");
//        }
        anonymousAccessAllowed(request, handler);
        setSecurityContextToRequest();
        return Boolean.TRUE;
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

    private boolean anonymousAccessAllowed(HttpServletRequest request, Object handler){
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            if (request.getDispatcherType().name().equals(REQUEST) && (method.getDeclaringClass().isAnnotationPresent(Controller.class)
                    || method.getDeclaringClass().isAnnotationPresent(RestController.class))) {
                if (method.isAnnotationPresent(AllowAnonymous.class)) {
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
        } else {
            LOGGER.info(ESAPI.encoder().encodeForHTML("Authorizing INTERNAL/EXTERNAL CLIENT!!"));
            if(validateRequest(request)) {
                setSecurityContextToRequest();
            }
        }
        return Boolean.TRUE;
    }

    private boolean validateRequest(HttpServletRequest request){
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(StringUtils.isNotBlank(authorizationHeader)){
            return true;
        }
        return true;
    }

    private void setSecurityContextToRequest(){
        UserRequestContextPrincipal userSecurityContextModel = new UserRequestContextPrincipal();
        List<SimpleGrantedAuthority> updatedPermissions = fillInGrants(userSecurityContextModel);
        Authentication auth = new UsernamePasswordAuthenticationToken(userSecurityContextModel, null, updatedPermissions);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }


    private List<SimpleGrantedAuthority> fillInGrants(UserRequestContextPrincipal userRequestContextPrincipal){
        List<SimpleGrantedAuthority> securityGrants = new ArrayList<>();
        if(Objects.nonNull(userRequestContextPrincipal) && CollectionUtils.isNotEmpty(userRequestContextPrincipal.getPrivilegeModels())){
            userRequestContextPrincipal.getPrivilegeModels().forEach(item->
                securityGrants.add(new SimpleGrantedAuthority(item.getName())));
        }
        else{
            securityGrants.add(new SimpleGrantedAuthority("Anonymous"));
        }
        return securityGrants;
    }
}
