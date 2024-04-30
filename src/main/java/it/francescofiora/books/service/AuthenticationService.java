package it.francescofiora.books.service;

import it.francescofiora.books.service.dto.AuthenticationDto;
import it.francescofiora.books.service.dto.SigninDto;

/**
 * Authentication Service.
 */
public interface AuthenticationService {

  String ENTITY_NAME = "SigninDto";

  /**
   * Sign in.
   *
   * @param signinDto the SigninDto
   * @return the AuthenticationDto
   */
  AuthenticationDto signin(SigninDto signinDto);

}
