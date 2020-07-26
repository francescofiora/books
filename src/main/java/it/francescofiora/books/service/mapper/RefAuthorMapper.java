package it.francescofiora.books.service.mapper;

import it.francescofiora.books.domain.Author;
import it.francescofiora.books.service.dto.RefAuthorDto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Author} and its DTO {@link RefAuthorDto}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RefAuthorMapper extends EntityMapper<RefAuthorDto, Author> {

  @Mapping(target = "firstName", ignore = true)
  @Mapping(target = "lastName", ignore = true)
  @Mapping(target = "titles", ignore = true)
  @Mapping(target = "removeTitle", ignore = true)
  Author toEntity(RefAuthorDto dto);

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
