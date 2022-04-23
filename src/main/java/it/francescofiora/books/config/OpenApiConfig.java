package it.francescofiora.books.config;

import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
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
