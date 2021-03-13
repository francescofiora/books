package it.francescofiora.books.util;

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

  public static AuthorDto createAuthorDto(final Long id) {
    AuthorDto authorDto = new AuthorDto();
    authorDto.setId(id);
    authorDto.setFirstName("John");
    authorDto.setLastName("Smith");
    return authorDto;
  }

  public static PublisherDto createPublisherDto(final Long id) {
    PublisherDto publisherDto = new PublisherDto();
    publisherDto.setId(id);
    publisherDto.setPublisherName("Peter");
    return publisherDto;
  }

  public static NewAuthorDto createNewAuthorDto() {
    NewAuthorDto authorDto = new NewAuthorDto();
    authorDto.setFirstName("John");
    authorDto.setLastName("Smith");
    return authorDto;
  }

  public static NewPublisherDto createNewPublisherDto() {
    NewPublisherDto publisherDto = new NewPublisherDto();
    publisherDto.setPublisherName("Name");
    return publisherDto;
  }

  public static NewTitleDto createNewTitleDto() {
    NewTitleDto titleDto = new NewTitleDto();
    titleDto.setTitle("Title");
    titleDto.setCopyright(2020);
    titleDto.setEditionNumber(10L);
    titleDto.setImageFile("path_image");
    titleDto.setLanguage(Language.ENGLISH);
    titleDto.setPrice(10L);
    titleDto.setPublisher(createRefPublisherDto(1L));
    titleDto.getAuthors().add(createRefAuthorDto(1L));
    return titleDto;
  }
  
  public static RefAuthorDto createRefAuthorDto(final Long id) {
    RefAuthorDto authorDto = new RefAuthorDto();
    authorDto.setId(id);
    return authorDto;
  }

  public static RefPublisherDto createRefPublisherDto(final Long id) {
    RefPublisherDto publisherDto = new RefPublisherDto();
    publisherDto.setId(id);
    return publisherDto;
  }

  public static TitleDto createTitleDto(final Long id) {
    TitleDto titleDto = new TitleDto();
    titleDto.setId(id);
    titleDto.setTitle("Title");
    titleDto.setCopyright(2020);
    titleDto.setEditionNumber(10L);
    titleDto.setImageFile("path_image");
    titleDto.setLanguage(Language.ENGLISH);
    titleDto.setPrice(10L);
    titleDto.setPublisher(createPublisherDto(1L));
    titleDto.getAuthors().add(createAuthorDto(1L));
    return titleDto;
  }

  public static UpdatebleTitleDto createUpdatebleTitleDto(final Long id) {
    UpdatebleTitleDto titleDto = new UpdatebleTitleDto();
    titleDto.setId(id);
    titleDto.setTitle("Title");
    titleDto.setCopyright(2020);
    titleDto.setEditionNumber(10L);
    titleDto.setImageFile("path_image");
    titleDto.setLanguage(Language.ENGLISH);
    titleDto.setPrice(10L);
    titleDto.setPublisher(createRefPublisherDto(1L));
    titleDto.getAuthors().add(createRefAuthorDto(1L));
    return titleDto;
  }

  private TestUtils() {}
}
