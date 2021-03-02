package it.francescofiora.books.service.dto;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

public class NewAuthorDtoTest {

  @Test
  public void dtoEqualsVerifier() throws Exception {
    NewAuthorDto authorDto1 = new NewAuthorDto();
    authorDto1.setFirstName("Name");
    authorDto1.setLastName("Surname");
    NewAuthorDto authorDto2 = new NewAuthorDto();
    assertThat(authorDto1).isNotEqualTo(authorDto2);
    authorDto2.setFirstName(authorDto1.getFirstName());
    authorDto2.setLastName(authorDto1.getLastName());
    assertThat(authorDto1).isEqualTo(authorDto2);
    authorDto2.setFirstName("NameDiff");
    assertThat(authorDto1).isNotEqualTo(authorDto2);
    authorDto1.setFirstName(null);
    assertThat(authorDto1).isNotEqualTo(authorDto2);

    authorDto1.setFirstName("Name");
    authorDto2.setLastName("Surname");
    authorDto2.setFirstName(authorDto1.getFirstName());
    authorDto2.setLastName("SurnameDiff");
    assertThat(authorDto1).isNotEqualTo(authorDto2);
    authorDto1.setLastName(null);
    assertThat(authorDto1).isNotEqualTo(authorDto2);
  }
  
}
