package com.foodhub.filter;

import com.foodhub.security.HubUserDetailsService;
import com.foodhub.security.JWTokenGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JWTRequestFilter extends OncePerRequestFilter {

    private Logger logger = LoggerFactory.getLogger(JWTRequestFilter.class.getName());

    @Autowired
    private HubUserDetailsService userDetailsService;

    @Autowired
    private JWTokenGenerator tokenGenerator;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
                throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        String jwToken = null;

        if(null != authorizationHeader
                && StringUtils.hasText(authorizationHeader)
                    && authorizationHeader.startsWith("Bearer ")){
            jwToken = authorizationHeader.substring(7);
        }

        if(StringUtils.hasText(jwToken) && tokenGenerator.validateToken(jwToken)){

            Long userId = tokenGenerator.getUserIdFromJWT(jwToken);
            UserDetails userDetails = userDetailsService.loadUserById(userId);

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            usernamePasswordAuthenticationToken
                    .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

            logger.info("Authentication of token is now complete.");

        }else{
            logger.error("Could not find the token in request header.");
        }

        filterChain.doFilter(request, response);
    }
}
