package com.foodhub.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT Authentication Entry Point for checking request authority
 */
@Component
public class JWTAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private Logger logger = LoggerFactory.getLogger(JWTAuthenticationEntryPoint.class.getName());

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException, ServletException {

        logger.error("Unauthorized request. "+ authException.getMessage());
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());

    }
}
