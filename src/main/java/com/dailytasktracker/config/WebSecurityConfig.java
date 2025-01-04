package com.dailytasktracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                // Allow access to static files (index.html, login.html, css, js, images)
                .antMatchers("/", "/index.html", "/login.html", "/account.html", "/signup.html","/css/**", "/js/**", "/img/**").permitAll()
                // Allow access to API endpoints for login and account creation
                .antMatchers("/api/**").permitAll()
                // All other requests require authentication
                .anyRequest().authenticated()
                .and()
                .cors().configurationSource(corsConfigurationSource()) // Add CORS configuration
                .and()
                .formLogin()
                .loginPage("/login") // Custom login page
                .permitAll()
                .and()
                .logout()
                .permitAll();
    }

    // CORS configuration to allow cross-origin requests
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");  // Allow all origins
        config.addAllowedHeader("*");  // Allow all headers
        config.addAllowedMethod("*");  // Allow all methods
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
