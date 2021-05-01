package com.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Optional;
import java.util.Arrays;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  private UserDetailsService userDetailsService;

  public SecurityConfig(UserDetailsService userSvc) {
    this.userDetailsService = userSvc;
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth
      .userDetailsService(userDetailsService)
      .passwordEncoder(encoder());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
      .cors().and().csrf().disable();
    http
      .requiresChannel()
      .anyRequest()
      .requiresSecure();
    http
      .httpBasic()
        .and()
      .authorizeRequests()
        .antMatchers(HttpMethod.POST, "/reset", "/reset/**").permitAll()
        .antMatchers(HttpMethod.GET, "/all", "/all/**").authenticated()
        .antMatchers(HttpMethod.GET, "/customers", "/customers/*").authenticated()
        .antMatchers(HttpMethod.POST, "/customers", "/customers/*").hasRole("MANAGER")
        .antMatchers(HttpMethod.PUT, "/customers", "/customers/*").authenticated()

        .antMatchers(HttpMethod.GET, "/contents", "/contents/*").authenticated()
        .antMatchers(HttpMethod.DELETE, "/contents", "/contents/*").hasAnyRole("MANAGER","ANALYST")
        .antMatchers(HttpMethod.POST, "/contents", "/contents/*").hasAnyRole("MANAGER", "ANALYST")
        .antMatchers(HttpMethod.PUT, "/contents", "/contents/*").hasAnyRole("MANAGER", "ANALYST")

        .antMatchers(HttpMethod.GET, "/accounts/*").hasRole("USER")
        .antMatchers(HttpMethod.POST, "/accounts").hasRole("MANAGER")
        .antMatchers(HttpMethod.POST, "/accounts/*/transactions").hasRole("USER")
        .antMatchers(HttpMethod.GET, "/accounts/*/transactions").hasRole("USER")

        .antMatchers(HttpMethod.GET, "/stocks", "/stocks/*").hasRole("USER")
        .antMatchers(HttpMethod.POST, "/trades").hasRole("USER")
        .antMatchers(HttpMethod.GET, "/trades", "/trades/*").hasRole("USER")
        .antMatchers(HttpMethod.PUT, "/trades/*").hasRole("USER")
        .antMatchers(HttpMethod.GET, "/portfolio").hasRole("USER")
        .and()
      .formLogin().disable();
  }

  @Bean
  public BCryptPasswordEncoder encoder() {
    // auto-generate a random salt internally
    return new BCryptPasswordEncoder();
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Arrays.asList("*"));
    configuration.setAllowedMethods(Arrays.asList("*"));
    configuration.setAllowedHeaders(Arrays.asList("*"));
    configuration.setAllowCredentials(true);
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}