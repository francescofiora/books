package it.francescofiora.books.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.books.util.TestUtils;
import org.junit.jupiter.api.Test;

class NewPublisherDtoTest {

  @Test
  void dtoEqualsVerifier() {
    var publisherDto1 = TestUtils.createNewPublisherDto();
    var publisherDto2 = new NewPublisherDto();
    assertThat(publisherDto1).isNotEqualTo(publisherDto2);

    publisherDto2 = TestUtils.createNewPublisherDto();
    TestUtils.checkEqualHashAndToString(publisherDto1, publisherDto2);

    publisherDto1.setPublisherName("DifName");
    TestUtils.checkNotEqualHashAndToString(publisherDto1, publisherDto2);

    publisherDto1.setPublisherName(null);
    assertThat(publisherDto1).isNotEqualTo(publisherDto2);
  }
}
