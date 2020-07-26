package it.francescofiora.books.service.mapper;

import it.francescofiora.books.domain.Title;
import it.francescofiora.books.service.dto.TitleDto;
import it.francescofiora.books.service.dto.UpdatebleTitleDto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Mapper for the entity {@link Title} and its DTO {@link TitleDto}.
 */
@Mapper(
    componentModel = "spring", uses = { PublisherMapper.class, AuthorMapper.class,
        RefPublisherMapper.class, RefAuthorMapper.class })
public interface TitleMapper extends EntityMapper<TitleDto, Title> {

  @Mapping(target = "removeAuthor", ignore = true)
  Title toEntity(TitleDto titleDto);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "removeAuthor", ignore = true)
  void updateEntityFromDto(UpdatebleTitleDto titleDto, @MappingTarget Title title);

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
