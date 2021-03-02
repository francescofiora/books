package it.francescofiora.books.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class RefPublisherDtoTest {

  @Test
  public void dtoEqualsVerifier() throws Exception {
    RefPublisherDto refPublisherDto1 = new RefPublisherDto();
    refPublisherDto1.setId(1L);
    RefPublisherDto refPublisherDto2 = new RefPublisherDto();
    assertThat(refPublisherDto1).isNotEqualTo(refPublisherDto2);
    refPublisherDto2.setId(refPublisherDto1.getId());
    assertThat(refPublisherDto1).isEqualTo(refPublisherDto2);
    refPublisherDto2.setId(2L);
    assertThat(refPublisherDto1).isNotEqualTo(refPublisherDto2);
    refPublisherDto1.setId(null);
    assertThat(refPublisherDto1).isNotEqualTo(refPublisherDto2);
  }

}
