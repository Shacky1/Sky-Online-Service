//package com.shacky.materialmanagement.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//
//@Configuration
//@EnableWebSecurity
//public class WebSecurityConfig {
//
//    // Configuring security filter chain for role-based authorization
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeRequests()
//                .antMatchers("/admin").hasRole("ADMIN")  // Only ADMIN users can access this page
//                .antMatchers("/user").hasAnyRole("USER", "ADMIN")  // Both USER and ADMIN can access this page
//                .anyRequest().authenticated()  // Any other request should be authenticated
//                .and()
//                .formLogin()
//                .loginPage("/login")  // Custom login page
//                .permitAll()
//                .and()
//                .logout()
//                .permitAll();  // Allow all users to logout
//
//        return http.build();
//    }
//
//    // Configure UserDetailsService to provide authentication data
//    @Bean
//    public UserDetailsService userDetailsService() {
//        return username -> {
//            // Here, you can retrieve user details from a database
//            // If you're testing, you can create an in-memory user:
//            if (username.equals("admin")) {
//                return User.withUsername("admin")
//                        .password(passwordEncoder().encode("password"))  // Password is encoded
//                        .roles("ADMIN")
//                        .build();
//            } else if (username.equals("user")) {
//                return User.withUsername("user")
//                        .password(passwordEncoder().encode("password"))  // Password is encoded
//                        .roles("USER")
//                        .build();
//            }
//            throw new UsernameNotFoundException("User not found");
//        };
//    }
//
//    // Password encoder to encrypt user passwords
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//}
