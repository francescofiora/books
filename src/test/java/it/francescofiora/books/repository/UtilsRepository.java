package it.francescofiora.books.repository;

import it.francescofiora.books.domain.Author;
import it.francescofiora.books.domain.Publisher;
import it.francescofiora.books.domain.Title;
import it.francescofiora.books.domain.enumeration.Language;
import java.util.Objects;

public class UtilsRepository {

  /**
   * Create first example of Author.
   *
   * @return Author
   */
  public static Author createAuthor1() {
    Author author = new Author();
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
    Author author = new Author();
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
    Author author = new Author();
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
    Publisher publisher = new Publisher();
    publisher.setPublisherName("Small Publisher Ltd");
    return publisher;
  }

  /**
   * Create second example of Publisher.
   *
   * @return Publisher
   */
  public static Publisher createPublisher2() {
    Publisher publisher = new Publisher();
    publisher.setPublisherName("Publisher Ltd");
    return publisher;
  }

  /**
   * Create third example of Publisher.
   *
   * @return Publisher
   */
  public static Publisher createPublisher3() {
    Publisher publisher = new Publisher();
    publisher.setPublisherName("Big Publisher Ltd");
    return publisher;
  }

  /**
   * Create first example of Title.
   *
   * @return Title
   */
  public static Title createTitle1() {
    Title title = new Title();
    title.getAuthors().add(createAuthor1());
    title.setCopyright(2020);
    title.setEditionNumber(1L);
    title.setImageFile("image.jpg");
    title.setLanguage(Language.ENGLISH);
    title.setPrice(10L);
    title.setPublisher(createPublisher1());
    title.setTitle("The Book");
    return title;
  }

  /**
   * Create second example of Title.
   *
   * @return Title
   */
  public static Title createTitle2() {
    Title title = new Title();
    title.getAuthors().add(createAuthor2());
    title.setCopyright(2019);
    title.setEditionNumber(2L);
    title.setImageFile("image.gif");
    title.setLanguage(Language.ITALIAN);
    title.setPrice(12L);
    title.setPublisher(createPublisher2());
    title.setTitle("The Big Book");
    return title;
  }

  /**
   * Create third example of Title.
   *
   * @return Title
   */
  public static Title createTitle3() {
    Title title = new Title();
    title.setCopyright(2018);
    title.setEditionNumber(3L);
    title.setImageFile("image00.gif");
    title.setLanguage(Language.SPANISH);
    title.setPrice(12L);
    title.setTitle("The Small Book");
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
        && Objects.equals(expected.getTitle(), actual.getTitle());
  }
}
