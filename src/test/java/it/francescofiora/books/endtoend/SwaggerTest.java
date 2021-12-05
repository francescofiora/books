package it.francescofiora.books.endtoend;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import it.francescofiora.books.web.api.AbstractTestApi;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Swagger UI Page and JSON Test.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource(locations = {"classpath:application_test.properties"})
class SwaggerTest extends AbstractTestApi {

  @Test
  void testSwaggerUiPage() throws Exception {
    performGet("/swagger-ui/index.html?configUrl=/books/v3/api-docs/swagger-config")
        .andExpect(status().isOk()).andExpect(content().string(containsString("Swagger UI")));
  }

  @Test
  void testJsonSwagger() throws Exception {
    performGet("/v3/api-docs").andExpect(status().isOk())
        .andExpect(content().string(containsString("Books Demo App")));
  }
}
