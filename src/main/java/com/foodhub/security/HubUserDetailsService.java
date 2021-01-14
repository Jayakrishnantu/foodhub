package com.foodhub.security;

import com.foodhub.entity.User;
import com.foodhub.repository.UserRepository;
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

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        User user = userRepo.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found: Username : "+ username)
        );

        return UserPrincipal.create(user);
    }

    @Transactional
    public UserDetails loadUserById(Long id)
            throws UsernameNotFoundException{

        User user = userRepo.findById(id)
                .orElseThrow( () -> new UsernameNotFoundException("User Not found with id "+ id)
                );

        return UserPrincipal.create(user);
    }
}
