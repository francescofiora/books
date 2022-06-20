package it.francescofiora.books.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Users Setup.
 */
@Configuration
public class UsersSetup {

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth,
      BCryptPasswordEncoder passwordEncoder, UserDetailsService userDetailsService)
      throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
  }
}
