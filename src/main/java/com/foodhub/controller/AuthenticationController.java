package com.foodhub.controller;

import com.foodhub.constants.HubConstants;
import com.foodhub.entity.UserRole;
import com.foodhub.exception.NotAuthorizedException;
import com.foodhub.payload.AuthenticationRequest;
import com.foodhub.payload.AuthenticationResponse;
import com.foodhub.payload.RegisterRequest;
import com.foodhub.repository.UserRepository;
import com.foodhub.security.JWTokenGenerator;
import com.foodhub.services.HubUserDetailsService;
import com.foodhub.util.HubUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Controller Handling User Authentication
 */
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

    @Autowired
    HubUserDetailsService service;

    @PostMapping(value="/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticateUser(
            @Valid @RequestBody AuthenticationRequest request){
        logger.debug("initiating authentication.");
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
        logger.debug("authentication now complete.");
        return ResponseEntity.ok(new AuthenticationResponse(jwToken));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest request){
        boolean invalidType = false;
        try{
            switch (request.getUserType()){
                case HubConstants.USER_TYPE_ADMIN:
                    request.setRole(UserRole.ROLE_ADMIN);
                    break;
                case HubConstants.USER_TYPE_CUSTOMER:
                    request.setRole(UserRole.ROLE_CUSTOMER);
                    break;
                case HubConstants.USER_TYPE_DRIVER:
                    request.setRole(UserRole.ROLE_DRIVER);
                    break;
                case HubConstants.USER_TYPE_SHOP:
                    request.setRole(UserRole.ROLE_SHOP);
                    break;
                default:
                    invalidType = true;
                    throw new Exception("Invalid User Type");
            }

            service.registerUser(request);
        }catch(Exception ex){
            logger.error("Error while creating user : ", ex);
            return new ResponseEntity<>(hubUtil.readMessage(invalidType ? "hub.signup.user.invalid.user.type" : "hub.signup.user.failure"), HttpStatus.OK);
        }
        return new ResponseEntity<>(hubUtil.readMessage("hub.signup.user.success"), HttpStatus.OK);
    }

    @PutMapping("/{username}")
    public ResponseEntity<?> updateUserInfo(@Valid @PathVariable("username") String userName){
        // TODO: To be implemented
        return new ResponseEntity<>("Service not implemented.", HttpStatus.NOT_IMPLEMENTED);
    }

    @GetMapping("/hello")
    public String sayHello(){
        return hubUtil.readMessage("default.title");
    }
}
