package it.francescofiora.books.service.impl;

import it.francescofiora.books.repository.AuthorRepository;
import it.francescofiora.books.service.AuthorService;
import it.francescofiora.books.service.dto.AuthorDto;
import it.francescofiora.books.service.dto.NewAuthorDto;
import it.francescofiora.books.service.dto.TitleDto;
import it.francescofiora.books.service.mapper.AuthorMapper;
import it.francescofiora.books.service.mapper.TitleMapper;
import it.francescofiora.books.web.errors.BadRequestAlertException;
import it.francescofiora.books.web.errors.NotFoundAlertException;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * AuthorService Impl class.
 */
@Service
@Transactional
public class AuthorServiceImpl implements AuthorService {

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  private final AuthorRepository authorRepository;

  private final AuthorMapper authorMapper;

  private final TitleMapper titleMapper;

  /**
   * Constructor.
   *
   * @param authorRepository AuthorRepository
   * @param authorMapper AuthorMapper
   */
  public AuthorServiceImpl(AuthorRepository authorRepository, AuthorMapper authorMapper,
      TitleMapper titleMapper) {
    this.authorRepository = authorRepository;
    this.authorMapper = authorMapper;
    this.titleMapper = titleMapper;
  }

  @Override
  public AuthorDto create(NewAuthorDto authorDto) {
    log.debug("Request to create a new Author : {}", authorDto);
    var author = authorMapper.toEntity(authorDto);
    author = authorRepository.save(author);
    return authorMapper.toDto(author);
  }

  @Override
  public void update(AuthorDto authorDto) {
    log.debug("Request to update Author : {}", authorDto);
    var authorOpt = authorRepository.findById(authorDto.getId());
    if (!authorOpt.isPresent()) {
      var id = String.valueOf(authorDto.getId());
      throw new NotFoundAlertException(ENTITY_NAME, id, ENTITY_NAME + " not found with id " + id);
    }
    var author = authorOpt.get();
    authorMapper.updateEntityFromDto(authorDto, author);
    authorRepository.save(author);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<AuthorDto> findAll(Pageable pageable) {
    log.debug("Request to get all Authors");
    return authorRepository.findAll(pageable).map(authorMapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<AuthorDto> findOne(Long id) {
    log.debug("Request to get Author : {}", id);
    return authorRepository.findById(id).map(authorMapper::toDto);
  }

  @Override
  public void delete(Long id) {
    log.debug("Request to delete Author : {}", id);
    var authorOpt = authorRepository.findById(id);
    if (authorOpt.isPresent() && !authorOpt.get().getTitles().isEmpty()) {
      throw new BadRequestAlertException(ENTITY_NAME, String.valueOf(id),
          "Almost a Title is using this author");
    }
    authorRepository.deleteById(id);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<TitleDto> findTitlesByAuthorId(Pageable pageable, Long id) {
    log.debug("Request to get Ttitles by Author id: {}", id);
    var authorOpt = authorRepository.findById(id);
    if (!authorOpt.isPresent()) {
      throw new NotFoundAlertException(ENTITY_NAME, String.valueOf(id),
          "Author Not Found with id " + id);
    }
    var author = authorOpt.get();
    return new PageImpl<TitleDto>(
        titleMapper.toDto(author.getTitles().stream().collect(Collectors.toList())));
  }
}
