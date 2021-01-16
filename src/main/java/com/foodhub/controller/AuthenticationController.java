package com.foodhub.controller;

import com.foodhub.exception.NotAuthorizedException;
import com.foodhub.payload.AuthenticationRequest;
import com.foodhub.payload.AuthenticationResponse;
import com.foodhub.security.JWTokenGenerator;
import com.foodhub.util.HubUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class AuthenticationController {

    Logger logger = LoggerFactory.getLogger(AuthenticationController.class.getName());

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    JWTokenGenerator jwTokenGenerator;

    @Autowired
    HubUtil hubUtil;

    @PostMapping(value="/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticateUser(
            @RequestBody AuthenticationRequest request) throws Exception {

        logger.info("initiating authentication.");

        Authentication authentication;
        try {
            authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword())
            );
        }catch(BadCredentialsException exception){
            logger.error("Authentication Failed: Invalid username or password.");
            throw new NotAuthorizedException(hubUtil.readMessage("hub.signin.error"));
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwToken = jwTokenGenerator.generateToken(authentication);

        logger.info("authentication now complete.");
        return ResponseEntity.ok(new AuthenticationResponse(jwToken));
    }

    @GetMapping("/hello")
    public String sayHello(){
        return hubUtil.readMessage("default.title");
    }
}
