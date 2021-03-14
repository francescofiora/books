package it.francescofiora.books.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.books.domain.enumeration.Language;
import it.francescofiora.books.util.TestUtils;
import java.util.Collections;
import org.junit.jupiter.api.Test;

public class NewTitleDtoTest {

  @Test
  public void dtoEqualsVerifier() throws Exception {
    NewTitleDto titleDto1 = TestUtils.createNewTitleDto();
    NewTitleDto titleDto2 = new NewTitleDto();
    assertThat(titleDto1).isNotEqualTo(titleDto2);

    titleDto2 = TestUtils.createNewTitleDto();
    assertThat(titleDto1).isEqualTo(titleDto2);

    titleDto2.setCopyright(2021);
    assertThat(titleDto1).isNotEqualTo(titleDto2);

    titleDto2 = TestUtils.createNewTitleDto();
    titleDto2.setEditionNumber(1L);
    assertThat(titleDto1).isNotEqualTo(titleDto2);

    titleDto2 = TestUtils.createNewTitleDto();
    titleDto2.setImageFile("ImageFileDiff");
    assertThat(titleDto1).isNotEqualTo(titleDto2);

    titleDto2 = TestUtils.createNewTitleDto();
    titleDto1.setLanguage(Language.ITALIAN);
    assertThat(titleDto1).isNotEqualTo(titleDto2);

    titleDto2 = TestUtils.createNewTitleDto();
    titleDto1.setPrice(11L);
    assertThat(titleDto1).isNotEqualTo(titleDto2);

    titleDto2 = TestUtils.createNewTitleDto();
    titleDto1.setPublisher(new RefPublisherDto());
    assertThat(titleDto1).isNotEqualTo(titleDto2);

    titleDto2 = TestUtils.createNewTitleDto();
    titleDto1.setTitle("TitleDiff");
    assertThat(titleDto1).isNotEqualTo(titleDto2);

    titleDto2 = TestUtils.createNewTitleDto();
    titleDto1.setAuthors(Collections.singletonList(new RefAuthorDto()));
    assertThat(titleDto1).isNotEqualTo(titleDto2);
  }
}
