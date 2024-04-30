package it.francescofiora.books.service.impl;

import it.francescofiora.books.repository.UserRepository;
import it.francescofiora.books.service.AuthenticationService;
import it.francescofiora.books.service.JwtService;
import it.francescofiora.books.service.dto.AuthenticationDto;
import it.francescofiora.books.service.dto.SigninDto;
import it.francescofiora.books.web.errors.BadRequestAlertException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Authentication Service Impl.
 */
@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

  private final UserRepository userRepository;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  @Override
  public AuthenticationDto signin(SigninDto signinDto) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(signinDto.getUsername(), signinDto.getPassword()));
    var user = userRepository.findByUsername(signinDto.getUsername())
        .orElseThrow(() ->
            new BadRequestAlertException("SigninDto", "", "Invalid username or password"));
    var result = new AuthenticationDto();
    result.setToken(jwtService.generateToken(user));
    return result;
  }
}
