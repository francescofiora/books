package it.francescofiora.books.service.mapper;

import it.francescofiora.books.domain.Author;
import it.francescofiora.books.service.dto.NewAuthorDto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Author} and its DTO {@link NewAuthorDto}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface NewAuthorMapper extends EntityMapper<NewAuthorDto, Author> {

  @Mapping(target = "titles", ignore = true)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "removeTitle", ignore = true)
  Author toEntity(NewAuthorDto authorDto);

  /**
   * create Author from id.
   * @param id id
   * @return Author
   */
  default Author fromId(Long id) {
    if (id == null) {
      return null;
    }
    Author author = new Author();
    author.setId(id);
    return author;
  }
}
