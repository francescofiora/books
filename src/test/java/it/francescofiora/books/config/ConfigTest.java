package it.francescofiora.books.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.junit.jupiter.api.Test;

/**
 * Configs Test.
 */
class ConfigTest {

  @Test
  void testJpaConfiguration() {
    assertDoesNotThrow(() -> new JpaConfiguration());
  }

  @Test
  void testPasswordEncoder() {
    var securityConfig = new SecurityConfig();
    var bce = securityConfig.passwordEncoder();
    assertThat(bce).isNotNull();
  }

  @Test
  void testCustomGlobalHeaders() {
    var operationCustomizer = new OpenApiConfig().customGlobalHeaders();

    var operation = operationCustomizer.customize(mock(Operation.class), null);
    verify(operation).addParametersItem(any(Parameter.class));
  }
}
