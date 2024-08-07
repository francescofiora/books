package it.francescofiora.books.service.impl;

import it.francescofiora.books.domain.Author;
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
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * AuthorService Impl class.
 */
@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class AuthorServiceImpl implements AuthorService {

  private static final GenericPropertyMatcher PROPERTY_MATCHER_DEFAULT =
      GenericPropertyMatchers.contains().ignoreCase();

  private final AuthorRepository authorRepository;
  private final AuthorMapper authorMapper;
  private final TitleMapper titleMapper;

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
    if (authorOpt.isEmpty()) {
      var id = String.valueOf(authorDto.getId());
      throw new NotFoundAlertException(ENTITY_NAME, id,
          String.format(NotFoundAlertException.MSG_NOT_FOUND_WITH_ID, ENTITY_NAME, id));
    }
    var author = authorOpt.get();
    authorMapper.updateEntityFromDto(authorDto, author);
    authorRepository.save(author);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<AuthorDto> findAll(String firstName, String lastName, Pageable pageable) {
    log.debug("Request to get all Authors");
    var author = new Author();
    author.setFirstName(firstName);
    author.setLastName(lastName);
    var exampleMatcher =
        ExampleMatcher.matchingAll().withMatcher("firstName", PROPERTY_MATCHER_DEFAULT)
            .withMatcher("lastName", PROPERTY_MATCHER_DEFAULT);
    var example = Example.of(author, exampleMatcher);
    return authorRepository.findAll(example, pageable).map(authorMapper::toDto);
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
    log.debug("Request to get Titles by Author id: {}", id);
    var authorOpt = authorRepository.findById(id);
    if (authorOpt.isEmpty()) {
      throw new NotFoundAlertException(ENTITY_NAME, String.valueOf(id),
          String.format(NotFoundAlertException.MSG_NOT_FOUND_WITH_ID, ENTITY_NAME, id));
    }
    var author = authorOpt.get();
    return new PageImpl<>(
        titleMapper.toDto(author.getTitles().stream().toList()));
  }
}
