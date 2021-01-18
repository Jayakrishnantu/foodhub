package com.foodhub.services;

import com.foodhub.entity.Role;
import com.foodhub.entity.User;
import com.foodhub.entity.UserRole;
import com.foodhub.exception.RegistrationException;
import com.foodhub.payload.RegisterRequest;
import com.foodhub.repository.UserRepository;
import com.foodhub.security.UserPrincipal;
import com.foodhub.util.HubUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

/**
 * User Details Service Implementation for Authentication
 */
@Service
public class HubUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepo;

    @Autowired
    HubUtil hubUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

    @Transactional
    public void registerUser(RegisterRequest request){

        if(userRepo.existsByUserName(request.getUsername())){
            throw new RegistrationException("Username already taken.");
        }

        User user = new User();
        user.setName(request.getName());
        user.setUserName(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setContact(request.getContact());
        if(null != request.getRestaurantId()) user.setRestaurantId(request.getRestaurantId());

        // Create Role and tie to the user.
        Role role = new Role();
        role.setRole(request.getRole());
        role.setUser(user);
        user.addRole(role);

        userRepo.save(user);
    }
}
