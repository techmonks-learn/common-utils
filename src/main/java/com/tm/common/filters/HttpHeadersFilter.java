package com.tm.common.filters;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(1)
public class HttpHeadersFilter implements Filter {

    @Value("${http-headers.access-control-allow-origin:*}")
    private String accessControlAllowOrigin;
    @Value("${http-headers.access-control-allow-methods:POST, GET, PUT, OPTIONS, DELETE, PATCH, HEAD}")
    private String accessControlAllowMethods;
    @Value("${http-headers.access-control-allow-headers:X-Auth-Token, Content-Type, X-Total-Count, UUID, Authentication}")
    private String accessControlAllowHeaders;
    @Value("${http-headers.access-control-expose-headers:X-Total-Count, UUID, Authentication}")
    private String accessControlExposeHeaders;
    @Value("${http-headers.access-control-allow-credentials:false}")
    private String accessControlAllowCredentials;
    @Value("${http-headers.access-control-max-age:4800}")
    private String accessControlMaxAge;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setHeader("Access-Control-Allow-Origin", accessControlAllowOrigin);
        httpResponse.setHeader("Access-Control-Allow-Methods", accessControlAllowMethods);
        httpResponse.setHeader("Access-Control-Allow-Headers", accessControlAllowHeaders);
        httpResponse.setHeader("Access-Control-Expose-Headers", accessControlExposeHeaders);
        httpResponse.setHeader("Access-Control-Allow-Credentials", accessControlAllowCredentials);
        httpResponse.setHeader("Access-Control-Max-Age", accessControlMaxAge);
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}
