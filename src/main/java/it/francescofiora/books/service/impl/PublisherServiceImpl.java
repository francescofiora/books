package it.francescofiora.books.service.impl;

import it.francescofiora.books.domain.Publisher;
import it.francescofiora.books.repository.PublisherRepository;
import it.francescofiora.books.repository.TitleRepository;
import it.francescofiora.books.service.PublisherService;
import it.francescofiora.books.service.dto.NewPublisherDto;
import it.francescofiora.books.service.dto.PublisherDto;
import it.francescofiora.books.service.mapper.PublisherMapper;
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
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * PublisherService Impl class.
 */
@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class PublisherServiceImpl implements PublisherService {

  private static final GenericPropertyMatcher PROPERTY_MATCHER_DEFAULT =
      GenericPropertyMatchers.contains().ignoreCase();

  private final PublisherRepository publisherRepository;
  private final PublisherMapper publisherMapper;
  private final TitleRepository titleRepository;

  @Override
  public PublisherDto create(NewPublisherDto publisherDto) {
    log.debug("Request to create a new Publisher : {}", publisherDto);
    var publisher = publisherMapper.toEntity(publisherDto);
    publisher = publisherRepository.save(publisher);
    return publisherMapper.toDto(publisher);
  }

  @Override
  public void update(PublisherDto publisherDto) {
    log.debug("Request to save Publisher : {}", publisherDto);
    var publisherOpt = publisherRepository.findById(publisherDto.getId());
    if (publisherOpt.isEmpty()) {
      final var id = String.valueOf(publisherDto.getId());
      throw new NotFoundAlertException(ENTITY_NAME, id,
          String.format(NotFoundAlertException.MSG_NOT_FOUND_WITH_ID, ENTITY_NAME, id));
    }
    var publisher = publisherOpt.get();
    publisherMapper.updateEntityFromDto(publisherDto, publisher);
    publisherRepository.save(publisher);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<PublisherDto> findAll(String publisherName, Pageable pageable) {
    log.debug("Request to get all Publishers");
    var publisher = new Publisher();
    publisher.setPublisherName(publisherName);
    var exampleMatcher =
        ExampleMatcher.matchingAll().withMatcher("publisherName", PROPERTY_MATCHER_DEFAULT);
    var example = Example.of(publisher, exampleMatcher);
    return publisherRepository.findAll(example, pageable).map(publisherMapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<PublisherDto> findOne(Long id) {
    log.debug("Request to get Publisher : {}", id);
    return publisherRepository.findById(id).map(publisherMapper::toDto);
  }

  @Override
  public void delete(Long id) {
    log.debug("Request to delete Publisher : {}", id);
    var titleOpt = titleRepository.findOneWithPublisherRelationships(id);
    if (titleOpt.isPresent()) {
      throw new BadRequestAlertException(ENTITY_NAME, String.valueOf(id),
          "Almost a Title is using this publisher");
    }
    publisherRepository.deleteById(id);
  }
}
