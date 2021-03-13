package it.francescofiora.books.service.mapper;

import it.francescofiora.books.domain.Publisher;
import it.francescofiora.books.service.dto.PublisherDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Mapper for the entity {@link Publisher} and its DTO {@link PublisherDto}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PublisherMapper {

  PublisherDto toDto(Publisher entity);

  @Mapping(target = "id", ignore = true)
  void updateEntityFromDto(PublisherDto publisherDto, @MappingTarget Publisher publisher);

  /**
   * create Publisher from id.
   *
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
