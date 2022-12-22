package it.francescofiora.books.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * BuildProperties Config Test.
 */
class BuildPropertiesConfigTest {

  @Test
  void testBuildPropertiesConfig() {
    var buildPropertiesConfig = new BuildPropertiesConfig();
    var buildProperties = buildPropertiesConfig.buildProperties();
    assertThat(buildProperties).isNotNull();
  }
}
