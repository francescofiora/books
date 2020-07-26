package it.francescofiora.books.service.mapper;

import it.francescofiora.books.domain.Publisher;
import it.francescofiora.books.service.dto.RefPublisherDto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Publisher} and its DTO {@link RefPublisherDto}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RefPublisherMapper extends EntityMapper<RefPublisherDto, Publisher> {

  @Mapping(target = "publisherName", ignore = true)
  Publisher toEntity(RefPublisherDto dto);

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
