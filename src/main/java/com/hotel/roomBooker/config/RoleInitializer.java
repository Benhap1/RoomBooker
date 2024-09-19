package com.hotel.roomBooker.config;

import com.hotel.roomBooker.model.UserRole;
import com.hotel.roomBooker.repository.UserRoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RoleInitializer implements CommandLineRunner {

    private final UserRoleRepository userRoleRepository;

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
    }
}
