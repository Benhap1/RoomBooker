package com.hotel.roomBooker.service;

import com.hotel.roomBooker.model.User;
import com.hotel.roomBooker.model.UserRole;
import com.hotel.roomBooker.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found"));

        UserRole userRole = user.getUserRole();
        if (userRole == null || userRole.getRoleName() == null) {
            throw new UsernameNotFoundException("User has no role assigned");
        }

        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority(userRole.getRoleName().toUpperCase())
        );

        return new CustomUserDetails(user.getUserName(), user.getPassword(), authorities);
    }
}