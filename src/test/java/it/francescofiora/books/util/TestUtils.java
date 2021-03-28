package it.francescofiora.books.util;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.books.domain.enumeration.Language;
import it.francescofiora.books.service.dto.AuthorDto;
import it.francescofiora.books.service.dto.NewAuthorDto;
import it.francescofiora.books.service.dto.NewPublisherDto;
import it.francescofiora.books.service.dto.NewTitleDto;
import it.francescofiora.books.service.dto.PublisherDto;
import it.francescofiora.books.service.dto.RefAuthorDto;
import it.francescofiora.books.service.dto.RefPublisherDto;
import it.francescofiora.books.service.dto.TitleDto;
import it.francescofiora.books.service.dto.UpdatebleTitleDto;

/**
 * Utility class for testing.
 */
public final class TestUtils {

  /**
   * Create an example of AuthorDto.
   *
   * @param id ID
   * @return AuthorDto
   */
  public static AuthorDto createAuthorDto(final Long id) {
    AuthorDto authorDto = new AuthorDto();
    authorDto.setId(id);
    authorDto.setFirstName("John");
    authorDto.setLastName("Smith");
    return authorDto;
  }

  /**
   * Create an example of PublisherDto.
   *
   * @param id ID
   * @return PublisherDto
   */
  public static PublisherDto createPublisherDto(final Long id) {
    PublisherDto publisherDto = new PublisherDto();
    publisherDto.setId(id);
    publisherDto.setPublisherName("Peter");
    return publisherDto;
  }

  /**
   * Create an example of NewAuthorDto.
   *
   * @return NewAuthorDto
   */
  public static NewAuthorDto createNewAuthorDto() {
    NewAuthorDto authorDto = new NewAuthorDto();
    authorDto.setFirstName("Robert");
    authorDto.setLastName("Smith");
    return authorDto;
  }

  /**
   * Create an example of NewPublisherDto.
   *
   * @return NewPublisherDto
   */
  public static NewPublisherDto createNewPublisherDto() {
    NewPublisherDto publisherDto = new NewPublisherDto();
    publisherDto.setPublisherName("Publisher");
    return publisherDto;
  }

  /**
   * Create an example of NewTitleDto with Publisher and Author.
   *
   * @return NewTitleDto
   */
  public static NewTitleDto createNewTitleDto() {
    NewTitleDto titleDto = createNewSimpleTitleDto();
    titleDto.setPublisher(createRefPublisherDto(1L));
    titleDto.getAuthors().add(createRefAuthorDto(1L));
    return titleDto;
  }

  /**
   * Create an example of NewTitleDto with no Publisher and no Author.
   *
   * @return NewTitleDto
   */
  public static NewTitleDto createNewSimpleTitleDto() {
    NewTitleDto titleDto = new NewTitleDto();
    titleDto.setTitle("The Title");
    titleDto.setCopyright(2020);
    titleDto.setEditionNumber(10L);
    titleDto.setImageFile("path_image");
    titleDto.setLanguage(Language.ENGLISH);
    titleDto.setPrice(15L);
    return titleDto;
  }

  /**
   * Create an example of RefAuthorDto.
   *
   * @param id ID
   * @return RefAuthorDto
   */
  public static RefAuthorDto createRefAuthorDto(final Long id) {
    RefAuthorDto authorDto = new RefAuthorDto();
    authorDto.setId(id);
    return authorDto;
  }

  /**
   * Create an example of RefPublisherDto.
   *
   * @param id ID
   * @return RefPublisherDto
   */
  public static RefPublisherDto createRefPublisherDto(final Long id) {
    RefPublisherDto publisherDto = new RefPublisherDto();
    publisherDto.setId(id);
    return publisherDto;
  }

  /**
   * Create an example of TitleDto with no Publisher and no Author.
   *
   * @param id ID
   * @return TitleDto
   */
  public static TitleDto createTitleDto(final Long id) {
    TitleDto titleDto = new TitleDto();
    titleDto.setId(id);
    titleDto.setTitle("One Title");
    titleDto.setCopyright(2021);
    titleDto.setEditionNumber(2L);
    titleDto.setImageFile("path_image");
    titleDto.setLanguage(Language.ITALIAN);
    titleDto.setPrice(12L);
    titleDto.setPublisher(createPublisherDto(2L));
    titleDto.getAuthors().add(createAuthorDto(2L));
    return titleDto;
  }

  /**
   * Create an example of UpdatebleTitleDto with Publisher and Author.
   *
   * @param id ID
   * @return UpdatebleTitleDto
   */
  public static UpdatebleTitleDto createUpdatebleTitleDto(final Long id) {
    UpdatebleTitleDto titleDto = createSimpleUpdatebleTitleDto(id);
    titleDto.setPublisher(createRefPublisherDto(3L));
    titleDto.getAuthors().add(createRefAuthorDto(3L));
    return titleDto;
  }

  /**
   * Create an example of UpdatebleTitleDto.
   *
   * @param id ID
   * @return UpdatebleTitleDto
   */
  public static UpdatebleTitleDto createSimpleUpdatebleTitleDto(final Long id) {
    UpdatebleTitleDto titleDto = new UpdatebleTitleDto();
    titleDto.setId(id);
    titleDto.setTitle("Simple Title");
    titleDto.setCopyright(2019);
    titleDto.setEditionNumber(8L);
    titleDto.setImageFile("path_image");
    titleDto.setLanguage(Language.SPANISH);
    titleDto.setPrice(11L);
    return titleDto;
  }

  /**
   * assert that obj1 is equal to obj2 and also their hashCode and ToString.
   *
   * @param obj1 the Object to compare
   * @param obj2 the Object to compare
   */
  public static void checkEqualHashAndToString(final Object obj1, final Object obj2) {
    assertThat(obj1.equals(obj2)).isTrue();
    assertThat(obj1.hashCode()).isEqualTo(obj2.hashCode());
    assertThat(obj1.toString()).isEqualTo(obj2.toString());
  }

  /**
   * assert that obj1 is not equal to obj2 and also their hashCode and ToString.
   *
   * @param obj1 the Object to compare
   * @param obj2 the Object to compare
   */
  public static void checkNotEqualHashAndToString(final Object obj1, final Object obj2) {
    assertThat(obj1.equals(obj2)).isFalse();
    assertThat(obj1.hashCode()).isNotEqualTo(obj2.hashCode());
    assertThat(obj1.toString()).isNotEqualTo(obj2.toString());
  }

  private TestUtils() {}
}
