package it.francescofiora.books.service.impl;

import it.francescofiora.books.domain.Title;
import it.francescofiora.books.repository.TitleRepository;
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

@Service
@Transactional
public class TitleServiceImpl implements TitleService {

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  private static final String ENTITY_NAME = "TitleDto";

  private final TitleRepository titleRepository;

  private final TitleMapper titleMapper;

  /**
   * Constructor.
   * 
   * @param titleRepository TitleRepository
   * @param titleMapper TitleMapper
   */
  public TitleServiceImpl(TitleRepository titleRepository, TitleMapper titleMapper) {
    this.titleRepository = titleRepository;
    this.titleMapper = titleMapper;
  }

  @Override
  public TitleDto create(NewTitleDto titleDto) {
    log.debug("Request to create Title : {}", titleDto);
    Title title = titleMapper.toEntity(titleDto);
    title = titleRepository.save(title);
    return titleMapper.toDto(title);
  }

  @Override
  public void update(UpdatebleTitleDto titleDto) {
    log.debug("Request to update Title : {}", titleDto);
    Optional<Title> titleOpt = titleRepository.findById(titleDto.getId());
    if (!titleOpt.isPresent()) {
      final String id = String.valueOf(titleDto.getId());
      throw new NotFoundAlertException(ENTITY_NAME, id, ENTITY_NAME + " not found with id " + id);
    }
    Title title = titleOpt.get();
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
