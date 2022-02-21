package it.francescofiora.books.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Open Api Configuration.
 */
@Configuration
public class OpenApiConfig {

  /**
   * Custom OpenAPI component.
   *
   * @return OpenAPI Bean
   */
  @Bean
  public OpenAPI customOpenApi() {
    // @formatter:off
    return new OpenAPI()
        .components(new Components())
        .info(new Info()
            .title("Books Demo App")
            .description("This is a sample Spring Boot RESTful service"));
    // @formatter:on
  }
}
