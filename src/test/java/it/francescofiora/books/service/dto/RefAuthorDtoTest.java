package it.francescofiora.books.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import it.francescofiora.books.TestUtil;

public class RefAuthorDtoTest {
  @Test
  public void dtoEqualsVerifier() throws Exception {
    TestUtil.equalsVerifier(AuthorDto.class);
    RefAuthorDto refAuthorDto1 = new RefAuthorDto();
    refAuthorDto1.setId(1L);
    RefAuthorDto refAuthorDto2 = new RefAuthorDto();
    assertThat(refAuthorDto1).isNotEqualTo(refAuthorDto2);
    refAuthorDto2.setId(refAuthorDto1.getId());
    assertThat(refAuthorDto1).isEqualTo(refAuthorDto2);
    refAuthorDto2.setId(2L);
    assertThat(refAuthorDto1).isNotEqualTo(refAuthorDto2);
    refAuthorDto1.setId(null);
    assertThat(refAuthorDto1).isNotEqualTo(refAuthorDto2);
  }

}
