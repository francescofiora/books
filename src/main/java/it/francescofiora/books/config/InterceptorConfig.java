package it.francescofiora.books.config;

import it.francescofiora.books.web.interceptor.EndPointInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Interceptor Config.
 */
@Profile("Logging")
@Configuration
public class InterceptorConfig {

  /**
   * Create WebMvcConfigurer to add EndPointInterceptor.
   *
   * @param endPointInterceptor the end-point Interceptor
   * @return the WebMvcConfigurer
   */
  @Bean
  public WebMvcConfigurer getWebMvcConfigurer(EndPointInterceptor endPointInterceptor) {
    return new WebMvcConfigurer() {
      @Override
      public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(endPointInterceptor);
      }
    };
  }
}
