package it.francescofiora.books.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.books.domain.enumeration.Language;
import it.francescofiora.books.util.TestUtils;
import java.util.Collections;
import org.junit.jupiter.api.Test;

class NewTitleDtoTest {

  @Test
  void dtoEqualsVerifier() throws Exception {
    var titleDto1 = TestUtils.createNewTitleDto();
    var titleDto2 = new NewTitleDto();
    assertThat(titleDto1).isNotEqualTo(titleDto2);

    titleDto2 = TestUtils.createNewTitleDto();
    TestUtils.checkEqualHashAndToString(titleDto1, titleDto2);

    titleDto2.setCopyright(2021);
    TestUtils.checkNotEqualHashAndToString(titleDto1, titleDto2);

    titleDto2 = TestUtils.createNewTitleDto();
    titleDto2.setEditionNumber(1L);
    TestUtils.checkNotEqualHashAndToString(titleDto1, titleDto2);

    titleDto2 = TestUtils.createNewTitleDto();
    titleDto2.setImageFile("ImageFileDiff");
    TestUtils.checkNotEqualHashAndToString(titleDto1, titleDto2);

    titleDto2 = TestUtils.createNewTitleDto();
    titleDto2.setLanguage(Language.ITALIAN);
    TestUtils.checkNotEqualHashAndToString(titleDto1, titleDto2);

    titleDto2 = TestUtils.createNewTitleDto();
    titleDto2.setPrice(11L);
    TestUtils.checkNotEqualHashAndToString(titleDto1, titleDto2);

    titleDto2 = TestUtils.createNewTitleDto();
    titleDto2.setPublisher(new RefPublisherDto());
    TestUtils.checkNotEqualHashAndToString(titleDto1, titleDto2);

    titleDto2 = TestUtils.createNewTitleDto();
    titleDto2.setTitle("TitleDiff");
    TestUtils.checkNotEqualHashAndToString(titleDto1, titleDto2);

    titleDto2 = TestUtils.createNewTitleDto();
    titleDto2.setAuthors(Collections.singletonList(new RefAuthorDto()));
    TestUtils.checkNotEqualHashAndToString(titleDto1, titleDto2);
  }
}
