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
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * TitleService Impl class.
 */
@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class TitleServiceImpl implements TitleService {

  private static final String ENTITY_NAME = "TitleDto";
  private static final String MSG_NOT_FOUND_WITH_ID = "%s not found with id %s";

  private final TitleRepository titleRepository;
  private final TitleMapper titleMapper;
  private final AuthorRepository authorRepository;
  private final PublisherRepository publisherRepository;

  @Override
  public TitleDto create(NewTitleDto titleDto) {
    log.debug("Request to create Title : {}", titleDto);

    if (!publisherRepository.findById(titleDto.getPublisher().getId()).isPresent()) {
      final var id = String.valueOf(titleDto.getPublisher().getId());
      throw new NotFoundAlertException(PublisherService.ENTITY_NAME, id,
          String.format(MSG_NOT_FOUND_WITH_ID, PublisherService.ENTITY_NAME, id));
    }

    for (var authorDto : titleDto.getAuthors()) {
      if (!authorRepository.findById(authorDto.getId()).isPresent()) {
        final var id = String.valueOf(authorDto.getId());
        throw new NotFoundAlertException(AuthorService.ENTITY_NAME, id,
            String.format(MSG_NOT_FOUND_WITH_ID, AuthorService.ENTITY_NAME, id));
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
      throw new NotFoundAlertException(ENTITY_NAME, id,
          String.format(MSG_NOT_FOUND_WITH_ID, ENTITY_NAME, id));
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
