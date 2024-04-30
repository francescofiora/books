package it.francescofiora.books.endtoend;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.books.util.UserUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = {"classpath:application_test.properties"})
class AuthenticationApiEndToEndTest extends AbstractTestEndToEnd {

  private static final String ALERT_BAD_REQUEST = "SigninDto.badRequest";
  private static final String PARAM_PASSWORD = "[signinDto.password - NotBlank]";
  private static final String PARAM_USERNAME = "[signinDto.username - NotBlank]";

  @Test
  void testSignIn() {
    var result = getToken(UserUtils.BOOK_ADMIN);
    assertThat(result.getToken()).isNotNull();
  }

  @Test
  void testBadRequest() {
    var auth = performLogin(UserUtils.BOOK_ADMIN, null);
    checkHeadersError(auth.getHeaders(), ALERT_BAD_REQUEST, PARAM_PASSWORD);
    assertThat(auth.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    auth = performLogin(UserUtils.BOOK_ADMIN, "");
    checkHeadersError(auth.getHeaders(), ALERT_BAD_REQUEST, PARAM_PASSWORD);
    assertThat(auth.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    auth = performLogin(null, UserUtils.PASSWORD);
    checkHeadersError(auth.getHeaders(), ALERT_BAD_REQUEST, PARAM_USERNAME);
    assertThat(auth.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    auth = performLogin("", UserUtils.PASSWORD);
    checkHeadersError(auth.getHeaders(), ALERT_BAD_REQUEST, PARAM_USERNAME);
    assertThat(auth.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    auth = performLogin("WRONG", UserUtils.PASSWORD);
    assertThat(auth.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);

    auth = performLogin(UserUtils.USER, "WRONG");
    assertThat(auth.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }
}
