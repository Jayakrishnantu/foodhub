package com.foodhub.util;

import com.foodhub.services.HubUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class HubSecurityUtil {

    public boolean checkUserIsAdmin(HubUserDetailsService userDetailsService, Long userId){

        UserDetails details = userDetailsService.loadUserById(userId);
        if (details != null && details.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return true;
        }
        return false;
    }

}
