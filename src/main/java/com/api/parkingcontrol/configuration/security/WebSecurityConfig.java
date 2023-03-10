package com.api.parkingcontrol.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

  @Autowired
  UserDetailsServiceImpl userDetailsService;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
    http
        .httpBasic()
        .and()
        .authorizeHttpRequests()
//        .requestMatchers(HttpMethod.GET, "/parking-spot/**").permitAll()
//        .requestMatchers(HttpMethod.POST, "/parking-spot").hasRole("USER")
//        .requestMatchers(HttpMethod.DELETE, "/parking-spot/**").hasRole("ADMIN")
        .anyRequest().authenticated()
        .and()
        .csrf().disable()
        .userDetailsService(userDetailsService);

    return http.build();
  }

//  @Bean
//  public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
//    UserDetails user = User.withUsername("user")
//        .password(passwordEncoder.encode("user"))
//        .roles("USER")
//        .build();
//
//    UserDetails admin = User.withUsername("admin")
//        .password(passwordEncoder.encode("admin"))
//        .roles("USER", "ADMIN")
//        .build();
//
//    return new InMemoryUserDetailsManager(user, admin);
//  }


  @Bean
  public PasswordEncoder passwordEncoder(){
    return new BCryptPasswordEncoder();
  }

}
