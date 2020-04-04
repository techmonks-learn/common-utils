package com.tm.common.filters;

import com.tm.common.constants.Constants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component("MDCFilter")
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MDCFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(MDCFilter.class);

    private FilterConfig filterConfig = null;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        LOGGER.info("MDC Filter Incorporated");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        MDC.clear();
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        MDC.put(Constants.MDC.X_REQUEST_PATH, request.getRequestURI());
        MDC.put(Constants.MDC.X_IP_ADDRESS, getClientIpAddress(request));

        chain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        // Not implemented.
    }

    private static String getClientIpAddress(HttpServletRequest request) {
        String remoteAddr = "";
        if (Objects.nonNull(request)) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (Objects.isNull(remoteAddr) || StringUtils.isEmpty(remoteAddr)) { //remoteAddr == null || "".equals(remoteAddr)
                remoteAddr = request.getRemoteAddr();
            } else {
                remoteAddr = remoteAddr.split(",")[0];
            }
        }
        return remoteAddr;
    }
}
