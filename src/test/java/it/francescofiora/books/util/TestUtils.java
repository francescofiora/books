package it.francescofiora.books.util;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.books.domain.Author;
import it.francescofiora.books.domain.DomainIdentifier;
import it.francescofiora.books.domain.Publisher;
import it.francescofiora.books.domain.Title;
import it.francescofiora.books.domain.enumeration.Language;
import it.francescofiora.books.service.dto.AuthorDto;
import it.francescofiora.books.service.dto.DtoIdentifier;
import it.francescofiora.books.service.dto.NewAuthorDto;
import it.francescofiora.books.service.dto.NewPublisherDto;
import it.francescofiora.books.service.dto.NewTitleDto;
import it.francescofiora.books.service.dto.PublisherDto;
import it.francescofiora.books.service.dto.RefAuthorDto;
import it.francescofiora.books.service.dto.RefPublisherDto;
import it.francescofiora.books.service.dto.TitleDto;
import it.francescofiora.books.service.dto.UpdatebleTitleDto;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * Utility for testing.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TestUtils {

  /**
   * Create first example of Author.
   *
   * @return Author
   */
  public static Author createAuthor1() {
    var author = new Author();
    author.setFirstName("John");
    author.setLastName("Smith");
    return author;
  }

  /**
   * Create second example of Author.
   *
   * @return Author
   */
  public static Author createAuthor2() {
    var author = new Author();
    author.setFirstName("Sophia");
    author.setLastName("Brown");
    return author;
  }

  /**
   * Create third example of Author.
   *
   * @return Author
   */
  public static Author createAuthor3() {
    var author = new Author();
    author.setFirstName("William");
    author.setLastName("Wilson");
    return author;
  }

  /**
   * Create first example of Publisher.
   *
   * @return Publisher
   */
  public static Publisher createPublisher1() {
    var publisher = new Publisher();
    publisher.setPublisherName("Small Publisher Ltd");
    return publisher;
  }

  /**
   * Create second example of Publisher.
   *
   * @return Publisher
   */
  public static Publisher createPublisher2() {
    var publisher = new Publisher();
    publisher.setPublisherName("Publisher Ltd");
    return publisher;
  }

  /**
   * Create third example of Publisher.
   *
   * @return Publisher
   */
  public static Publisher createPublisher3() {
    var publisher = new Publisher();
    publisher.setPublisherName("Big Publisher Ltd");
    return publisher;
  }

  /**
   * Create first example of Title.
   *
   * @return Title
   */
  public static Title createTitle1() {
    var title = new Title();
    title.getAuthors().add(createAuthor1());
    title.setCopyright(2020);
    title.setEditionNumber(1L);
    title.setImageFile("image1.jpg");
    title.setLanguage(Language.ENGLISH);
    title.setPrice(10L);
    title.setPublisher(createPublisher1());
    title.setName("The Book");
    return title;
  }

  /**
   * Create second example of Title.
   *
   * @return Title
   */
  public static Title createTitle2() {
    var title = new Title();
    title.getAuthors().add(createAuthor2());
    title.setCopyright(2019);
    title.setEditionNumber(2L);
    title.setImageFile("image2.gif");
    title.setLanguage(Language.ITALIAN);
    title.setPrice(12L);
    title.setPublisher(createPublisher2());
    title.setName("The Big Book");
    return title;
  }

  /**
   * Create third example of Title.
   *
   * @return Title
   */
  public static Title createTitle3() {
    var title = new Title();
    title.setCopyright(2018);
    title.setEditionNumber(3L);
    title.setImageFile("image00.gif");
    title.setLanguage(Language.SPANISH);
    title.setPrice(12L);
    title.setName("The Small Book");
    return title;
  }

  /**
   * Check that actual data equals expected data.
   *
   * @param expected Author
   * @param actual Author
   * @return true if actual contains same data then expected
   */
  public static boolean dataEquals(Author expected, Author actual) {
    return Objects.equals(expected.getFirstName(), actual.getFirstName())
        && Objects.equals(expected.getLastName(), actual.getLastName());
  }

  /**
   * Check that actual data equals expected data.
   *
   * @param expected Publisher
   * @param actual Publisher
   * @return true if actual contains same data then expected
   */
  public static boolean dataEquals(Publisher expected, Publisher actual) {
    return Objects.equals(expected.getPublisherName(), actual.getPublisherName());
  }

  /**
   * Check that actual data equals expected data.
   *
   * @param expected Title
   * @param actual Title
   * @return true if actual contains same data then expected
   */
  public static boolean dataEquals(Title expected, Title actual) {
    return Objects.equals(expected.getCopyright(), actual.getCopyright())
        && Objects.equals(expected.getEditionNumber(), actual.getEditionNumber())
        && Objects.equals(expected.getImageFile(), actual.getImageFile())
        && Objects.equals(expected.getLanguage(), actual.getLanguage())
        && Objects.equals(expected.getPrice(), actual.getPrice())
        && Objects.equals(expected.getName(), actual.getName());
  }

  /**
   * Create an example of AuthorDto.
   *
   * @param id ID
   * @return AuthorDto
   */
  public static AuthorDto createAuthorDto(final Long id) {
    var authorDto = new AuthorDto();
    authorDto.setId(id);
    authorDto.setFirstName("John");
    authorDto.setLastName("Anderson");
    return authorDto;
  }

  /**
   * Create an example of PublisherDto.
   *
   * @param id ID
   * @return PublisherDto
   */
  public static PublisherDto createPublisherDto(final Long id) {
    var publisherDto = new PublisherDto();
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
    var authorDto = new NewAuthorDto();
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
    var publisherDto = new NewPublisherDto();
    publisherDto.setPublisherName("Publisher");
    return publisherDto;
  }

  /**
   * Create an example of NewTitleDto with Publisher and Author.
   *
   * @return NewTitleDto
   */
  public static NewTitleDto createNewTitleDto() {
    var titleDto = createNewSimpleTitleDto();
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
    var titleDto = new NewTitleDto();
    titleDto.setName("The Title");
    titleDto.setCopyright(2020);
    titleDto.setEditionNumber(10L);
    titleDto.setImageFile("image3.gif");
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
    var authorDto = new RefAuthorDto();
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
    var publisherDto = new RefPublisherDto();
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
    var titleDto = new TitleDto();
    titleDto.setId(id);
    titleDto.setName("One Title");
    titleDto.setCopyright(2021);
    titleDto.setEditionNumber(2L);
    titleDto.setImageFile("image4.jpg");
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
    var titleDto = createSimpleUpdatebleTitleDto(id);
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
    var titleDto = new UpdatebleTitleDto();
    titleDto.setId(id);
    titleDto.setName("Simple Title");
    titleDto.setCopyright(2019);
    titleDto.setEditionNumber(8L);
    titleDto.setImageFile("path_image");
    titleDto.setLanguage(Language.SPANISH);
    titleDto.setPrice(11L);
    return titleDto;
  }

  /**
   * Assert that obj1 is equal to obj2 and also their hashCode and ToString.
   *
   * @param obj1 the Object to compare
   * @param obj2 the Object to compare
   */
  public static void checkEqualHashAndToString(final Object obj1, final Object obj2) {
    assertThat(obj1).isEqualTo(obj2);
    assertThat(obj1).hasSameHashCodeAs(obj2.hashCode());
    assertThat(obj1).hasToString(obj2.toString());
  }

  /**
   * Assert that obj1 is not equal to obj2 and also their hashCode and ToString.
   *
   * @param obj1 the Object to compare
   * @param obj2 the Object to compare
   */
  public static void checkNotEqualHashAndToString(final Object obj1, final Object obj2) {
    assertThat(obj1).isNotEqualTo(obj2);
    assertThat(obj1.hashCode()).isNotEqualTo(obj2.hashCode());
    assertThat(obj1.toString()).isNotEqualTo(obj2.toString());
  }

  /**
   * Create new DomainIdentifier.
   *
   * @param clazz the DomainIdentifier class.
   * @param id the id
   * @return a new DomainIdentifier
   * @throws Exception if error occurs
   */
  public static <T> DomainIdentifier createNewDomain(Class<T> clazz, Long id) throws Exception {
    var domainObj = (DomainIdentifier) clazz.getConstructor().newInstance();
    domainObj.setId(id);
    return domainObj;
  }

  /**
   * Create new DtoIdentifier.
   *
   * @param clazz the DtoIdentifier class.
   * @param id the id
   * @return a new DtoIdentifier
   * @throws Exception if error occurs
   */
  public static <T> DtoIdentifier createNewDtoIdentifier(Class<T> clazz, Long id) throws Exception {
    var dtoObj = (DtoIdentifier) clazz.getConstructor().newInstance();
    dtoObj.setId(id);
    return dtoObj;
  }

  /**
   * Create PageRequest As MultiValueMap.
   *
   * @param page the page
   * @param size the size
   * @return the MultiValueMap
   */
  public static MultiValueMap<String, String> createPageRequestAsMap(int page, int size) {
    MultiValueMap<String, String> pageRequest = new LinkedMultiValueMap<>();
    pageRequest.add("page", String.valueOf(page));
    pageRequest.add("size", String.valueOf(size));
    return pageRequest;
  }
}
