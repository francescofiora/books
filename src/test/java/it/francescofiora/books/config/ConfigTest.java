package it.francescofiora.books.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;

/**
 * Configs Test.
 */
class ConfigTest {

  @Test
  void testJpaConfiguration() throws Exception {
    assertDoesNotThrow(() -> new JpaConfiguration());
  }

  @Test
  void testOpenApiConfig() {
    OpenAPI openApi = new OpenApiConfig().customOpenApi();

    assertThat(openApi.getInfo().getTitle()).isEqualTo("Books Demo App");
    assertThat(openApi.getInfo().getDescription())
        .isEqualTo("This is a sample Spring Boot RESTful service");
  }
}
