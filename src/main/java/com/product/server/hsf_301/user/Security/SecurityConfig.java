package com.product.server.hsf_301.user.Security;

import com.product.server.hsf_301.user.model.MyAppUserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

//    private final HttpSecurity httpSecurity;
//
//    public SecurityConfig(HttpSecurity httpSecurity) {
//        this.httpSecurity = httpSecurity;
//    }

    @Autowired
    private final MyAppUserService appUserService;

    @Bean
    public UserDetailsService userDetailsService() {
        return appUserService;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(appUserService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();

    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/admin/**").hasRole("ADMIN");
                    auth.requestMatchers("/req/signup", "/css/**", "/js/**", "/req/login").permitAll();
                    auth.anyRequest().authenticated();
                })
                .logout(logout -> logout
                        .logoutUrl("/logout")                    // Default: /logout
                        .logoutSuccessUrl("/req/login?logout")   // Redirect after logout
                        .invalidateHttpSession(true)            // Invalidate session
                        .clearAuthentication(true)              // Clear authentication
                        .deleteCookies("JSESSIONID")            // Delete cookies
                        .permitAll()                            // Allow all users to logout
                )
                .formLogin(form -> {
                    form.loginPage("/req/login").permitAll();
                    form.defaultSuccessUrl("/", true);
                    form.successHandler((request, response, authentication) -> {
                        boolean isAdmin = authentication.getAuthorities().stream()
                            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
                        if (isAdmin) {
                            response.sendRedirect("/admin");
                        } else {
                            response.sendRedirect("/");
                        }
                    });
                })
                .build();
    }
}