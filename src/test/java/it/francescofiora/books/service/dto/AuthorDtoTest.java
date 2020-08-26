package it.francescofiora.books.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import it.francescofiora.books.TestUtil;

public class AuthorDtoTest {

  @Test
  public void dtoEqualsVerifier() throws Exception {
    TestUtil.equalsVerifier(AuthorDto.class);
    AuthorDto authorDto1 = new AuthorDto();
    authorDto1.setId(1L);
    AuthorDto authorDto2 = new AuthorDto();
    assertThat(authorDto1).isNotEqualTo(authorDto2);
    authorDto2.setId(authorDto1.getId());
    assertThat(authorDto1).isEqualTo(authorDto2);
    authorDto2.setId(2L);
    assertThat(authorDto1).isNotEqualTo(authorDto2);
    authorDto1.setId(null);
    assertThat(authorDto1).isNotEqualTo(authorDto2);
  }
}
