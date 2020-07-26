package it.francescofiora.books.service.mapper;

import it.francescofiora.books.domain.Publisher;
import it.francescofiora.books.service.dto.NewPublisherDto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Publisher} and its DTO {@link NewPublisherDto}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface NewPublisherMapper extends EntityMapper<NewPublisherDto, Publisher> {

  @Mapping(target = "id", ignore = true)
  Publisher toEntity(NewPublisherDto dto);

  /**
   * create Publisher from id.
   * @param id id
   * @return Publisher
   */
  default Publisher fromId(Long id) {
    if (id == null) {
      return null;
    }
    Publisher publisher = new Publisher();
    publisher.setId(id);
    return publisher;
  }
}
