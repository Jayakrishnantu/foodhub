package com.foodhub.security;

import com.foodhub.filter.JWTRequestFilter;
import com.foodhub.services.HubUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security Configuration for Food Hub App
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        prePostEnabled = true
)
public class FoodHubSecurity extends WebSecurityConfigurerAdapter {

    private Logger logger = LoggerFactory.getLogger(FoodHubSecurity.class.getName());

    @Autowired
    private HubUserDetailsService appUserDetailService;

    @Autowired
    private JWTRequestFilter jwtRequestFilter;

    @Autowired
    private JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(appUserDetailService)
                .passwordEncoder(passwordEncoder());
        logger.info("Set the User detail service and password encoding.");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .authorizeRequests()
                    .antMatchers("/api/user/authenticate").permitAll()
                    .antMatchers("/api/order/invoice/**").permitAll()
                    //.antMatchers("/api/test/**").permitAll()
                .anyRequest().authenticated()
                .and()
                    .exceptionHandling()
                    .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        logger.info("Security Configuration updated.");
    }
}
