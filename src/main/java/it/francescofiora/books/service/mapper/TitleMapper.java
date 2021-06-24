package it.francescofiora.books.service.mapper;

import it.francescofiora.books.domain.Title;
import it.francescofiora.books.service.dto.NewTitleDto;
import it.francescofiora.books.service.dto.TitleDto;
import it.francescofiora.books.service.dto.UpdatebleTitleDto;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Mapper for the entity {@link Title} and its DTO {@link TitleDto}.
 */
@Mapper(componentModel = "spring", uses = {PublisherMapper.class, AuthorMapper.class})
public interface TitleMapper {

  TitleDto toDto(Title entity);

  List<TitleDto> toDto(List<Title> entityList);

  @Mapping(target = "id", ignore = true)
  void updateEntityFromDto(UpdatebleTitleDto titleDto, @MappingTarget Title title);

  @Mapping(target = "id", ignore = true)
  Title toEntity(NewTitleDto dto);

  /**
   * Create Title from id.
   *
   * @param id id
   * @return Title
   */
  default Title fromId(Long id) {
    if (id == null) {
      return null;
    }
    var title = new Title();
    title.setId(id);
    return title;
  }
}
