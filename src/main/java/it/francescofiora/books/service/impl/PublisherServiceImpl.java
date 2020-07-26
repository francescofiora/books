package it.francescofiora.books.service.impl;

import it.francescofiora.books.domain.Publisher;
import it.francescofiora.books.repository.PublisherRepository;
import it.francescofiora.books.service.PublisherService;
import it.francescofiora.books.service.dto.NewPublisherDto;
import it.francescofiora.books.service.dto.PublisherDto;
import it.francescofiora.books.service.mapper.NewPublisherMapper;
import it.francescofiora.books.service.mapper.PublisherMapper;
import it.francescofiora.books.web.errors.NotFoundAlertException;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Publisher}.
 */
@Service
@Transactional
public class PublisherServiceImpl implements PublisherService {

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  private static final String ENTITY_NAME = "Publisher";

  private final PublisherRepository publisherRepository;

  private final PublisherMapper publisherMapper;

  private final NewPublisherMapper newPublisherMapper;

  /**
   * Constructor.
   * @param publisherRepository PublisherRepository
   * @param publisherMapper PublisherMapper
   * @param newPublisherMapper NewPublisherMapper
   */
  public PublisherServiceImpl(PublisherRepository publisherRepository,
      PublisherMapper publisherMapper, NewPublisherMapper newPublisherMapper) {
    this.publisherRepository = publisherRepository;
    this.publisherMapper = publisherMapper;
    this.newPublisherMapper = newPublisherMapper;
  }

  /**
   * Create a publisher.
   *
   * @param publisherDto the entity to save.
   * @return the persisted entity.
   */
  @Override
  public PublisherDto create(NewPublisherDto publisherDto) {
    log.debug("Request to create a new Publisher : {}", publisherDto);
    Publisher publisher = newPublisherMapper.toEntity(publisherDto);
    publisher = publisherRepository.save(publisher);
    return publisherMapper.toDto(publisher);
  }

  /**
   * Update a publisher.
   *
   * @param publisherDto the entity to save.
   */
  @Override
  public void update(PublisherDto publisherDto) {
    log.debug("Request to save Publisher : {}", publisherDto);
    Optional<Publisher> publisherOpt = publisherRepository.findById(publisherDto.getId());
    if (!publisherOpt.isPresent()) {
      throw new NotFoundAlertException(ENTITY_NAME);
    }
    Publisher publisher = publisherOpt.get();
    publisherMapper.updateEntityFromDto(publisherDto, publisher);
    publisherRepository.save(publisher);
  }

  /**
   * Get all the publishers.
   *
   * @param pageable the pagination information.
   * @return the list of entities.
   */
  @Override
  @Transactional(readOnly = true)
  public Page<PublisherDto> findAll(Pageable pageable) {
    log.debug("Request to get all Publishers");
    return publisherRepository.findAll(pageable).map(publisherMapper::toDto);
  }

  /**
   * Get one publisher by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Override
  @Transactional(readOnly = true)
  public Optional<PublisherDto> findOne(Long id) {
    log.debug("Request to get Publisher : {}", id);
    return publisherRepository.findById(id).map(publisherMapper::toDto);
  }

  /**
   * Delete the publisher by id.
   *
   * @param id the id of the entity.
   */
  @Override
  public void delete(Long id) {
    log.debug("Request to delete Publisher : {}", id);
    publisherRepository.deleteById(id);
  }
}
