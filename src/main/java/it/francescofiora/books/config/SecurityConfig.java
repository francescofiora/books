package it.francescofiora.books.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security Configuration.
 */
@Configuration
public class SecurityConfig {

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * Configure Basic authentication.
   *
   * @param http HttpSecurity
   * @return SecurityFilterChain
   * @throws Exception if errors occur
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    // @formatter:off
    http.authorizeRequests()
      .antMatchers().authenticated()
      .anyRequest().authenticated()
      .and().httpBasic().and().csrf().disable();
    // @formatter:on
    return http.build();
  }
}
