package it.francescofiora.books.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.books.util.TestUtils;
import org.junit.jupiter.api.Test;

public class NewAuthorDtoTest {

  @Test
  public void dtoEqualsVerifier() throws Exception {
    NewAuthorDto authorDto1 = TestUtils.createNewAuthorDto();
    NewAuthorDto authorDto2 = new NewAuthorDto();
    assertThat(authorDto1).isNotEqualTo(authorDto2);

    authorDto2 = TestUtils.createNewAuthorDto();
    assertThat(authorDto1).isEqualTo(authorDto2);
    authorDto2.setFirstName("NameDiff");
    assertThat(authorDto1).isNotEqualTo(authorDto2);
    authorDto1.setFirstName(null);
    assertThat(authorDto1).isNotEqualTo(authorDto2);

    authorDto1 = TestUtils.createNewAuthorDto();
    authorDto2 = TestUtils.createNewAuthorDto();
    authorDto2.setLastName("SurnameDiff");
    assertThat(authorDto1).isNotEqualTo(authorDto2);
    authorDto1.setLastName(null);
    assertThat(authorDto1).isNotEqualTo(authorDto2);
  }
  
}
