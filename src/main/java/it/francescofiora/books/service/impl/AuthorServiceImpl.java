package it.francescofiora.books.service.impl;

import it.francescofiora.books.domain.Author;
import it.francescofiora.books.repository.AuthorRepository;
import it.francescofiora.books.service.AuthorService;
import it.francescofiora.books.service.dto.AuthorDto;
import it.francescofiora.books.service.dto.NewAuthorDto;
import it.francescofiora.books.service.mapper.AuthorMapper;
import it.francescofiora.books.service.mapper.NewAuthorMapper;
import it.francescofiora.books.web.errors.NotFoundAlertException;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Author}.
 */
@Service
@Transactional
public class AuthorServiceImpl implements AuthorService {

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  private static final String ENTITY_NAME = "Author";

  private final AuthorRepository authorRepository;

  private final AuthorMapper authorMapper;

  private final NewAuthorMapper newAuthorMapper;

  /**
   * Constructor.
   * @param authorRepository AuthorRepository
   * @param authorMapper AuthorMapper
   * @param newAuthorMapper NewAuthorMapper
   */
  public AuthorServiceImpl(AuthorRepository authorRepository, AuthorMapper authorMapper,
      NewAuthorMapper newAuthorMapper) {
    this.authorRepository = authorRepository;
    this.authorMapper = authorMapper;
    this.newAuthorMapper = newAuthorMapper;
  }

  /**
   * Create a new author.
   *
   * @param authorDto the entity to save.
   * @return the persisted entity.
   */
  @Override
  public AuthorDto create(NewAuthorDto authorDto) {
    log.debug("Request to create a new Author : {}", authorDto);
    Author author = newAuthorMapper.toEntity(authorDto);
    author = authorRepository.save(author);
    return authorMapper.toDto(author);
  }

  /**
   * Update an author.
   *
   * @param authorDto the entity to save.
   */
  @Override
  public void update(AuthorDto authorDto) {
    log.debug("Request to update Author : {}", authorDto);
    Optional<Author> authorOpt = authorRepository.findById(authorDto.getId());
    if (!authorOpt.isPresent()) {
      throw new NotFoundAlertException(ENTITY_NAME);
    }
    Author author = authorOpt.get();
    authorMapper.updateEntityFromDto(authorDto, author);
    authorRepository.save(author);
  }

  /**
   * Get all the authors.
   *
   * @param pageable the pagination information.
   * @return the list of entities.
   */
  @Override
  @Transactional(readOnly = true)
  public Page<AuthorDto> findAll(Pageable pageable) {
    log.debug("Request to get all Authors");
    return authorRepository.findAll(pageable).map(authorMapper::toDto);
  }

  /**
   * Get one author by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Override
  @Transactional(readOnly = true)
  public Optional<AuthorDto> findOne(Long id) {
    log.debug("Request to get Author : {}", id);
    return authorRepository.findById(id).map(authorMapper::toDto);
  }

  /**
   * Delete the author by id.
   *
   * @param id the id of the entity.
   */
  @Override
  public void delete(Long id) {
    log.debug("Request to delete Author : {}", id);
    authorRepository.deleteById(id);
  }
}
