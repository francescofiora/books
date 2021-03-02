package it.francescofiora.books.service.dto;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import it.francescofiora.books.domain.enumeration.Language;

public class NewTitleDtoTest {

  @Test
  public void dtoEqualsVerifier() throws Exception {
    NewTitleDto titleDto1 = new NewTitleDto();
    titleDto1.setAuthors(Collections.emptyList());
    NewTitleDto titleDto2 = new NewTitleDto();
    assertThat(titleDto1).isNotEqualTo(titleDto2);

    titleDto1.setCopyright(2020);
    titleDto2.setAuthors(titleDto1.getAuthors());
    assertThat(titleDto1).isNotEqualTo(titleDto2);
    titleDto2.setCopyright(titleDto1.getCopyright());
    
    titleDto1.setEditionNumber(1L);
    assertThat(titleDto1).isNotEqualTo(titleDto2);
    titleDto2.setEditionNumber(titleDto1.getEditionNumber());

    titleDto1.setImageFile("ImageFile");
    assertThat(titleDto1).isNotEqualTo(titleDto2);
    titleDto2.setImageFile(titleDto1.getImageFile());

    titleDto1.setLanguage(Language.ENGLISH);
    assertThat(titleDto1).isNotEqualTo(titleDto2);

    titleDto1.setPrice(10L);
    titleDto2.setLanguage(titleDto1.getLanguage());
    assertThat(titleDto1).isNotEqualTo(titleDto2);
    titleDto2.setPrice(titleDto1.getPrice());

    titleDto1.setPublisher(new RefPublisherDto());
    assertThat(titleDto1).isNotEqualTo(titleDto2);
    titleDto2.setPublisher(titleDto1.getPublisher());

    titleDto1.setTitle("Title");
    assertThat(titleDto1).isNotEqualTo(titleDto2);
    titleDto2.setTitle(titleDto1.getTitle());
    
    titleDto1.setAuthors(Collections.singletonList(new RefAuthorDto()));
    assertThat(titleDto1).isNotEqualTo(titleDto2);
    titleDto1.setAuthors(null);
    assertThat(titleDto1).isNotEqualTo(titleDto2);
    titleDto2.setAuthors(titleDto1.getAuthors());

    titleDto1.setCopyright(2021);
    assertThat(titleDto1).isNotEqualTo(titleDto2);
    titleDto1.setCopyright(null);
    assertThat(titleDto1).isNotEqualTo(titleDto2);
    titleDto2.setCopyright(titleDto1.getCopyright());

    titleDto1.setEditionNumber(2L);
    assertThat(titleDto1).isNotEqualTo(titleDto2);
    titleDto1.setEditionNumber(null);
    assertThat(titleDto1).isNotEqualTo(titleDto2);
    titleDto2.setEditionNumber(titleDto1.getEditionNumber());

    titleDto1.setImageFile("ImageFileDiff");
    assertThat(titleDto1).isNotEqualTo(titleDto2);
    titleDto1.setImageFile(null);
    assertThat(titleDto1).isNotEqualTo(titleDto2);
    titleDto2.setImageFile(titleDto1.getImageFile());
  
    titleDto1.setLanguage(Language.ITALIAN);
    assertThat(titleDto1).isNotEqualTo(titleDto2);
    titleDto1.setLanguage(null);
    assertThat(titleDto1).isNotEqualTo(titleDto2);
    titleDto2.setLanguage(titleDto1.getLanguage());
    
    titleDto1.setPrice(20L);
    assertThat(titleDto1).isNotEqualTo(titleDto2);
    titleDto1.setPrice(null);
    assertThat(titleDto1).isNotEqualTo(titleDto2);
    titleDto2.setPrice(titleDto1.getPrice());

    titleDto1.setPublisher(new RefPublisherDto());
    assertThat(titleDto1).isNotEqualTo(titleDto2);
    titleDto1.setPublisher(null);
    assertThat(titleDto1).isNotEqualTo(titleDto2);
    titleDto2.setPublisher(titleDto1.getPublisher());
  
    titleDto1.setTitle("TitleDiff");
    assertThat(titleDto1).isNotEqualTo(titleDto2);
    titleDto1.setTitle(null);
    assertThat(titleDto1).isNotEqualTo(titleDto2);
  }
}
