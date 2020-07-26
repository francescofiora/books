package it.francescofiora.books.repository;

import it.francescofiora.books.domain.Author;
import it.francescofiora.books.domain.Publisher;
import it.francescofiora.books.domain.Title;
import it.francescofiora.books.domain.enumeration.Language;

public class UtilsRepository {

  public static Author createAuthor1() {
    return new Author().firstName("John").lastName("Smith");
  }

  public static Author createAuthor2() {
    return new Author().firstName("Sophia").lastName("Brown");
  }

  public static Author createAuthor3() {
    return new Author().firstName("William").lastName("Wilson");
  }

  public static boolean assertEquals(Author expected, Author actual) {
    return expected.getFirstName().equals(actual.getFirstName())
        && expected.getLastName().equals(actual.getLastName());
  }

  public static Publisher createPublisher1() {
    return new Publisher().publisherName("Small Publisher Ltd");
  }

  public static Publisher createPublisher2() {
    return new Publisher().publisherName("Publisher Ltd");
  }

  public static Publisher createPublisher3() {
    return new Publisher().publisherName("Big Publisher Ltd");
  }

  public static boolean assertEquals(Publisher expected, Publisher actual) {
    return expected.getPublisherName().equals(actual.getPublisherName());
  }

  public static Title createTitle1() {
    return new Title().addAuthor(createAuthor1()).copyright(2020).editionNumber(1L)
        .imageFile("image.jpg").language(Language.ENGLISH).price(10L).publisher(createPublisher1())
        .title("The Book");
  }

  public static Title createTitle2() {
    return new Title().addAuthor(createAuthor2()).copyright(2019).editionNumber(2L)
        .imageFile("image.gif").language(Language.ITALIAN).price(12L).publisher(createPublisher2())
        .title("The Big Book");
  }

  public static Title createTitle3() {
    return new Title().copyright(2018).editionNumber(3L).imageFile("image00.gif")
        .language(Language.SPANISH).price(12L).title("The Small Book");
  }

  public static boolean assertEquals(Title expecteted, Title actual) {
    return expecteted.getCopyright().equals(actual.getCopyright())
        && expecteted.getEditionNumber().equals(actual.getEditionNumber())
        && expecteted.getImageFile().equals(actual.getImageFile())
        && expecteted.getLanguage().equals(actual.getLanguage())
        && expecteted.getPrice().equals(actual.getPrice())
        && expecteted.getTitle().equals(actual.getTitle());
  }
}
