package it.francescofiora.books.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import it.francescofiora.books.web.filter.EndPointFilter;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Open Api Configuration.
 */
@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Books Demo App",
        description = "This is a sample Spring Boot RESTful service"))
@SecurityScheme(
    name = "Bearer Authentication",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer"
)
public class OpenApiConfig {

  /**
   * Custom GlobalHeaders component.
   *
   * @return OperationCustomizer Bean
   */
  @Bean
  public OperationCustomizer customGlobalHeaders() {

    return (operation, handlerMethod) -> {

      var requestIdParam =
          new Parameter().in(ParameterIn.HEADER.toString()).schema(new StringSchema())
              .name(EndPointFilter.X_REQUEST_ID).description("Request ID").required(false);

      operation.addParametersItem(requestIdParam);

      return operation;
    };
  }
}
