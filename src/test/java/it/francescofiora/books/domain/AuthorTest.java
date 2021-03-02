package it.francescofiora.books.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class AuthorTest {

  @Test
  public void equalsVerifier() throws Exception {
    Author author1 = new Author();
    author1.setId(1L);
    Author author2 = new Author();
    author2.setId(author1.getId());
    assertThat(author1).isEqualTo(author2);
    author2.setId(2L);
    assertThat(author1).isNotEqualTo(author2);
    author1.setId(null);
    assertThat(author1).isNotEqualTo(author2);
  }
}
