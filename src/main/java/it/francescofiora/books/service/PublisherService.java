package it.francescofiora.books.service;

import it.francescofiora.books.service.dto.NewPublisherDto;
import it.francescofiora.books.service.dto.PublisherDto;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Publisher Service.
 */
public interface PublisherService {

  String ENTITY_NAME = "PublisherDto";

  /**
   * Create a new publisher.
   *
   * @param publisherDto the entity to save.
   * @return the persisted entity.
   */
  PublisherDto create(NewPublisherDto publisherDto);

  /**
   * Update a publisher.
   *
   * @param publisherDto the entity to save.
   */
  void update(PublisherDto publisherDto);

  /**
   * Get all the publishers.
   *
   * @param pageable the pagination information.
   * @return the list of entities.
   */
  Page<PublisherDto> findAll(Pageable pageable);

  /**
   * Get the "id" publisher.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  Optional<PublisherDto> findOne(Long id);

  /**
   * Delete the "id" publisher.
   *
   * @param id the id of the entity.
   */
  void delete(Long id);
}
