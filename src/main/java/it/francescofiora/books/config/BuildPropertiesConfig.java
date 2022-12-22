package it.francescofiora.books.config;

import java.util.Properties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * BuildProperties Config.
 */
@Configuration
public class BuildPropertiesConfig {

  /**
   * Create a new BuildProperties if missing (Work around for Eclipse).
   *
   * @return a BuildProperties
   */
  @Bean
  @ConditionalOnMissingBean(BuildProperties.class)
  public BuildProperties buildProperties() {
    return new BuildProperties(new Properties());
  }
}
