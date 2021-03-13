package it.francescofiora.books.repository;

import it.francescofiora.books.domain.Author;
import it.francescofiora.books.domain.Publisher;
import it.francescofiora.books.domain.Title;
import it.francescofiora.books.domain.enumeration.Language;
import java.util.Objects;

public class UtilsRepository {

  public static Author createAuthor1() {
    Author author = new Author();
    author.setFirstName("John");
    author.setLastName("Smith");
    return author;
  }

  public static Author createAuthor2() {
    Author author = new Author();
    author.setFirstName("Sophia");
    author.setLastName("Brown");
    return author;
  }

  public static Author createAuthor3() {
    Author author = new Author();
    author.setFirstName("William");
    author.setLastName("Wilson");
    return author;
  }

  public static boolean assertEquals(Author expected, Author actual) {
    return Objects.equals(expected.getFirstName(), actual.getFirstName())
        && Objects.equals(expected.getLastName(), actual.getLastName());
  }

  public static Publisher createPublisher1() {
    Publisher publisher = new Publisher();
    publisher.setPublisherName("Small Publisher Ltd");
    return publisher;
  }

  public static Publisher createPublisher2() {
    Publisher publisher = new Publisher();
    publisher.setPublisherName("Publisher Ltd");
    return publisher;
  }

  public static Publisher createPublisher3() {
    Publisher publisher = new Publisher();
    publisher.setPublisherName("Big Publisher Ltd");
    return publisher;
  }

  public static boolean assertEquals(Publisher expected, Publisher actual) {
    return Objects.equals(expected.getPublisherName(), actual.getPublisherName());
  }

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

  public static boolean assertEquals(Title expecteted, Title actual) {
    return Objects.equals(expecteted.getCopyright(), actual.getCopyright())
        && Objects.equals(expecteted.getEditionNumber(), actual.getEditionNumber())
        && Objects.equals(expecteted.getImageFile(), actual.getImageFile())
        && Objects.equals(expecteted.getLanguage(), actual.getLanguage())
        && Objects.equals(expecteted.getPrice(), actual.getPrice())
        && Objects.equals(expecteted.getTitle(), actual.getTitle());
  }
}
