package it.francescofiora.books.service.mapper;

import it.francescofiora.books.domain.Title;
import it.francescofiora.books.service.dto.NewTitleDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Title} and its DTO {@link NewTitleDto}.
 */
@Mapper(componentModel = "spring", uses = {RefPublisherMapper.class, RefAuthorMapper.class})
public interface NewTitleMapper {

  @Mapping(target = "id", ignore = true)
  Title toEntity(NewTitleDto dto);

  /**
   * create Title from id.
   *
   * @param id id
   * @return Title
   */
  default Title fromId(Long id) {
    if (id == null) {
      return null;
    }
    Title title = new Title();
    title.setId(id);
    return title;
  }
}
