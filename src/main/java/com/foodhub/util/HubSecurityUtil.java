package com.foodhub.util;

import com.foodhub.security.HubUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;

public class HubSecurityUtil {

    public static boolean checkUserIsAdmin(HubUserDetailsService userDetailsService, Long userId){

        UserDetails details = userDetailsService.loadUserById(userId);
        if (details != null && details.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
            return true;
        }
        return false;
    }

}
