package it.francescofiora.books.service;

import it.francescofiora.books.service.dto.NewTitleDto;
import it.francescofiora.books.service.dto.TitleDto;
import it.francescofiora.books.service.dto.UpdatebleTitleDto;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Title Service.
 */
public interface TitleService {

  /**
   * Create a new title.
   *
   * @param titleDto the entity to save.
   * @return the persisted entity.
   */
  TitleDto create(NewTitleDto titleDto);

  /**
   * Update a title.
   *
   * @param titleDto the entity to save.
   */
  void update(UpdatebleTitleDto titleDto);

  /**
   * Get all the titles.
   *
   * @param name the name.
   * @param pageable the pagination information.
   * @return the list of entities.
   */
  Page<TitleDto> findAll(String name, Pageable pageable);

  /**
   * Get the "id" title.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  Optional<TitleDto> findOne(Long id);

  /**
   * Delete the "id" title.
   *
   * @param id the id of the entity.
   */
  void delete(Long id);
}
