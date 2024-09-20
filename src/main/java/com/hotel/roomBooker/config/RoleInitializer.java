package com.hotel.roomBooker.config;

import com.hotel.roomBooker.exception.ResourceNotFoundException;
import com.hotel.roomBooker.model.User;
import com.hotel.roomBooker.model.UserRole;
import com.hotel.roomBooker.repository.UserRepository;
import com.hotel.roomBooker.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleInitializer implements CommandLineRunner {

    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (!userRoleRepository.existsByRoleName("ADMIN")) {
            UserRole adminRole = new UserRole();
            adminRole.setRoleName("ADMIN");
            userRoleRepository.save(adminRole);
        }

        if (!userRoleRepository.existsByRoleName("USER")) {
            UserRole userRole = new UserRole();
            userRole.setRoleName("USER");
            userRoleRepository.save(userRole);
        }

        if (!userRepository.existsByUserName("ADMIN")) {
            UserRole adminRole = userRoleRepository.findByRoleName("ADMIN")
                    .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
            User adminUser = new User();
            adminUser.setUserName("ADMIN");
            adminUser.setPassword(passwordEncoder.encode("ADMIN"));
            adminUser.setEmail("admin@example.com");
            adminUser.setUserRole(adminRole);
            userRepository.save(adminUser);
        }
    }
}
