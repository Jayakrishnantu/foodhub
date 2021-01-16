package com.foodhub.security;

import com.foodhub.entity.User;
import com.foodhub.repository.UserRepository;
import com.foodhub.util.HubUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class HubUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepo;

    @Autowired
    HubUtil hubUtil;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User user = userRepo.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        hubUtil.readMessage("hub.auth.user.not.found")+ username)
        );
        return UserPrincipal.create(user);
    }

    @Transactional
    public UserDetails loadUserById(Long id)
            throws UsernameNotFoundException{
        User user = userRepo.findById(id)
                .orElseThrow( () -> new UsernameNotFoundException(
                        hubUtil.readMessage("hub.auth.user.not.found.id")+ id)
        );
        return UserPrincipal.create(user);
    }
}
