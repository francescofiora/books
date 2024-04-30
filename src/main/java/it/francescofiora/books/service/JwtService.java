package it.francescofiora.books.service;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * Jwt Service.
 */
public interface JwtService {
  String extractUserName(String token);

  String generateToken(UserDetails userDetails);

  boolean isTokenValid(String token, UserDetails userDetails);
}
