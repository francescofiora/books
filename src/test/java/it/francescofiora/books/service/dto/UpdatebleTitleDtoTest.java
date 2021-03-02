package it.francescofiora.books.service.dto;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

public class UpdatebleTitleDtoTest {

  @Test
  public void dtoEqualsVerifier() throws Exception {
    UpdatebleTitleDto titleDto1 = new UpdatebleTitleDto();
    titleDto1.setId(1L);
    UpdatebleTitleDto titleDto2 = new UpdatebleTitleDto();
    assertThat(titleDto1).isNotEqualTo(titleDto2);
    titleDto2.setId(titleDto1.getId());
    assertThat(titleDto1).isEqualTo(titleDto2);
    titleDto2.setId(2L);
    assertThat(titleDto1).isNotEqualTo(titleDto2);
    titleDto1.setId(null);
    assertThat(titleDto1).isNotEqualTo(titleDto2);
  }
}
