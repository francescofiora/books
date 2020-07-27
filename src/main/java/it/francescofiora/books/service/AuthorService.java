package it.francescofiora.books.service;

import it.francescofiora.books.service.dto.AuthorDto;
import it.francescofiora.books.service.dto.NewAuthorDto;
import it.francescofiora.books.service.dto.TitleDto;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuthorService {

  /**
   * Create a new author.
   *
   * @param authorDto the entity to save.
   * @return the persisted entity.
   */
  AuthorDto create(NewAuthorDto authorDto);

  /**
   * Update an author.
   *
   * @param authorDto the entity to update.
   */
  void update(AuthorDto authorDto);

  /**
   * Get all the authors.
   *
   * @param pageable the pagination information.
   * @return the list of entities.
   */
  Page<AuthorDto> findAll(Pageable pageable);

  /**
   * Get by "id" author.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  Optional<AuthorDto> findOne(Long id);

  /**
   * Delete the "id" author.
   *
   * @param id the id of the entity.
   */
  void delete(Long id);

  /**
   * Get titles by author 'id'.
   * @param pageable Pageable
   * @param id id
   * @return the list of titles
   */
  Page<TitleDto> findTitlesByAuthorId(Pageable pageable, Long id);
}
