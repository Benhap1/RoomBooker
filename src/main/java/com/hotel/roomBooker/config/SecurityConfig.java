package com.hotel.roomBooker.config;

import com.hotel.roomBooker.service.CustomUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/user/**").hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/user/**").hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/user/**").hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/user/**").hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/hotels/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/hotels/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/hotels/{id}/rating").hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/hotels/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/hotels/filter").hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/rooms/filter").hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/rooms/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/rooms/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/rooms/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/booking").hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/booking/all").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/statistics/export").hasAuthority("ADMIN")
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .userDetailsService(customUserDetailsService);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
