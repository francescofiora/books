package it.francescofiora.books.web.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.francescofiora.books.service.AuthenticationService;
import it.francescofiora.books.service.dto.AuthenticationDto;
import it.francescofiora.books.service.dto.SigninDto;
import it.francescofiora.books.web.util.HeaderUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Authentication Api RestController.
 */
@RestController
@Tag(name = "authentication", description = "Authentication Rest API")
@RequestMapping("/api/v1/auth")
public class AuthenticationApi extends AbstractApi {

  private static final String ENTITY_NAME = "AuthenticationDto";
  private static final String TAG = "authentication";

  private final AuthenticationService authenticationService;

  public AuthenticationApi(AuthenticationService authenticationService) {
    super(ENTITY_NAME);
    this.authenticationService = authenticationService;
  }

  /**
   * {@code POST  /authors} : Sign-in and create a new token.
   *
   * @param signinDto the sign-in
   * @return the {@link ResponseEntity}
   */
  @Operation(summary = "Sign-in", description = "Sign-in and create a new token",
      tags = {TAG})
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Token created"),
      @ApiResponse(responseCode = "400", description = "Invalid input, object invalid")})
  @PostMapping("/login")
  public ResponseEntity<AuthenticationDto> signin(
      @Parameter(description = "Sign-in") @Valid @RequestBody SigninDto signinDto) {
    var result = authenticationService.signin(signinDto);
    return ResponseEntity
        .ok()
        .headers(HeaderUtil.createEntityGetAlert(ENTITY_NAME, "token"))
        .body(result);
  }
}
