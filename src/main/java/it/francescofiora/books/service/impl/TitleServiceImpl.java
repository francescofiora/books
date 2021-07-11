package it.francescofiora.books.service.impl;

import it.francescofiora.books.repository.AuthorRepository;
import it.francescofiora.books.repository.PublisherRepository;
import it.francescofiora.books.repository.TitleRepository;
import it.francescofiora.books.service.AuthorService;
import it.francescofiora.books.service.PublisherService;
import it.francescofiora.books.service.TitleService;
import it.francescofiora.books.service.dto.NewTitleDto;
import it.francescofiora.books.service.dto.TitleDto;
import it.francescofiora.books.service.dto.UpdatebleTitleDto;
import it.francescofiora.books.service.mapper.TitleMapper;
import it.francescofiora.books.web.errors.NotFoundAlertException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * TitleService Impl class.
 */
@Service
@Transactional
public class TitleServiceImpl implements TitleService {

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  private static final String ENTITY_NAME = "TitleDto";

  private final TitleRepository titleRepository;

  private final TitleMapper titleMapper;

  private final AuthorRepository authorRepository;

  private final PublisherRepository publisherRepository;

  /**
   * Constructor.
   *
   * @param titleRepository TitleRepository
   * @param titleMapper TitleMapper
   * @param authorRepository AuthorRepository
   * @param publisherRepository PublisherRepository
   */
  public TitleServiceImpl(TitleRepository titleRepository, TitleMapper titleMapper,
      AuthorRepository authorRepository, PublisherRepository publisherRepository) {
    this.titleRepository = titleRepository;
    this.titleMapper = titleMapper;
    this.authorRepository = authorRepository;
    this.publisherRepository = publisherRepository;
  }

  @Override
  public TitleDto create(NewTitleDto titleDto) {
    log.debug("Request to create Title : {}", titleDto);

    if (!publisherRepository.findById(titleDto.getPublisher().getId()).isPresent()) {
      final var id = String.valueOf(titleDto.getPublisher().getId());
      throw new NotFoundAlertException(PublisherService.ENTITY_NAME, id,
          PublisherService.ENTITY_NAME + " not found with id " + id);
    }

    for (var authorDto : titleDto.getAuthors()) {
      if (!authorRepository.findById(authorDto.getId()).isPresent()) {
        final var id = String.valueOf(authorDto.getId());
        throw new NotFoundAlertException(AuthorService.ENTITY_NAME, id,
            AuthorService.ENTITY_NAME + " not found with id " + id);
      }
    }

    var title = titleMapper.toEntity(titleDto);
    title = titleRepository.save(title);
    return titleMapper.toDto(title);
  }

  @Override
  public void update(UpdatebleTitleDto titleDto) {
    log.debug("Request to update Title : {}", titleDto);
    var titleOpt = titleRepository.findById(titleDto.getId());
    if (!titleOpt.isPresent()) {
      final var id = String.valueOf(titleDto.getId());
      throw new NotFoundAlertException(ENTITY_NAME, id, ENTITY_NAME + " not found with id " + id);
    }
    var title = titleOpt.get();
    titleMapper.updateEntityFromDto(titleDto, title);
    titleRepository.save(title);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<TitleDto> findAll(Pageable pageable) {
    log.debug("Request to get all Titles");
    return titleRepository.findAll(pageable).map(titleMapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<TitleDto> findOne(Long id) {
    log.debug("Request to get Title : {}", id);
    return titleRepository.findById(id).map(titleMapper::toDto);
  }

  @Override
  public void delete(Long id) {
    log.debug("Request to delete Title : {}", id);
    titleRepository.deleteById(id);
  }
}
