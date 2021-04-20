package it.francescofiora.books.service.mapper;

import it.francescofiora.books.domain.Author;
import it.francescofiora.books.service.dto.AuthorDto;
import it.francescofiora.books.service.dto.NewAuthorDto;
import it.francescofiora.books.service.dto.RefAuthorDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Mapper for the entity {@link Author} and its DTO {@link AuthorDto}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AuthorMapper {

  AuthorDto toDto(Author entity);

  @Mapping(target = "titles", ignore = true)
  @Mapping(target = "id", ignore = true)
  void updateEntityFromDto(AuthorDto authorDto, @MappingTarget Author author);

  @Mapping(target = "titles", ignore = true)
  @Mapping(target = "id", ignore = true)
  Author toEntity(NewAuthorDto authorDto);

  @Mapping(target = "firstName", ignore = true)
  @Mapping(target = "lastName", ignore = true)
  @Mapping(target = "titles", ignore = true)
  Author toEntity(RefAuthorDto dto);

  /**
   * create Author from id.
   *
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
