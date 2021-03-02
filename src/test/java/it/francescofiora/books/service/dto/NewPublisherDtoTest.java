package it.francescofiora.books.service.dto;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

public class NewPublisherDtoTest {

  @Test
  public void dtoEqualsVerifier() throws Exception {
    NewPublisherDto publisherDto1 = new NewPublisherDto();
    publisherDto1.setPublisherName("Name");
    NewPublisherDto publisherDto2 = new NewPublisherDto();
    assertThat(publisherDto1).isNotEqualTo(publisherDto2);
    publisherDto2.setPublisherName(publisherDto1.getPublisherName());
    assertThat(publisherDto1).isEqualTo(publisherDto2);
    publisherDto1.setPublisherName(null);
    assertThat(publisherDto1).isNotEqualTo(publisherDto2);
  }
}
