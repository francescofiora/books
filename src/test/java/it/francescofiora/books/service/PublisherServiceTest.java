package it.francescofiora.books.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import it.francescofiora.books.domain.Publisher;
import it.francescofiora.books.repository.PublisherRepository;
import it.francescofiora.books.repository.TitleRepository;
import it.francescofiora.books.service.dto.NewPublisherDto;
import it.francescofiora.books.service.dto.PublisherDto;
import it.francescofiora.books.service.impl.PublisherServiceImpl;
import it.francescofiora.books.service.mapper.PublisherMapper;
import it.francescofiora.books.web.errors.NotFoundAlertException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

class PublisherServiceTest {

  private static final Long ID = 1L;

  @Test
  void testCreate() {
    var publisherRepository = mock(PublisherRepository.class);
    var publisherMapper = mock(PublisherMapper.class);
    var publisherService =
        new PublisherServiceImpl(publisherRepository, publisherMapper, mock(TitleRepository.class));

    var publisher = new Publisher();
    when(publisherMapper.toEntity(any(NewPublisherDto.class))).thenReturn(publisher);
    when(publisherRepository.save(any(Publisher.class))).thenReturn(publisher);

    var expected = new PublisherDto();
    when(publisherMapper.toDto(any(Publisher.class))).thenReturn(expected);

    var publisherDto = new NewPublisherDto();
    var actual = publisherService.create(publisherDto);
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void testUpdateNotFound() {
    var publisherService = new PublisherServiceImpl(mock(PublisherRepository.class),
        mock(PublisherMapper.class), mock(TitleRepository.class));

    var publisherDto = new PublisherDto();
    assertThrows(NotFoundAlertException.class, () -> publisherService.update(publisherDto));
  }

  @Test
  void testUpdate() {
    var publisherRepository = mock(PublisherRepository.class);
    var publisherMapper = mock(PublisherMapper.class);
    var publisherService =
        new PublisherServiceImpl(publisherRepository, publisherMapper, mock(TitleRepository.class));

    var publisher = new Publisher();
    when(publisherRepository.findById(ID)).thenReturn(Optional.of(publisher));

    var publisherDto = new PublisherDto();
    publisherDto.setId(ID);
    publisherService.update(publisherDto);
    verify(publisherMapper).updateEntityFromDto(publisherDto, publisher);
    verify(publisherRepository).save(publisher);
  }

  @Test
  void testFindAll() {
    var publisherRepository = mock(PublisherRepository.class);
    var publisherMapper = mock(PublisherMapper.class);
    var publisherService =
        new PublisherServiceImpl(publisherRepository, publisherMapper, mock(TitleRepository.class));

    var publisher = new Publisher();
    when(publisherRepository.findAll(any(Pageable.class)))
        .thenReturn(new PageImpl<>(List.of(publisher)));
    var expected = new PublisherDto();
    when(publisherMapper.toDto(any(Publisher.class))).thenReturn(expected);
    var pageable = PageRequest.of(1, 1);
    var page = publisherService.findAll(pageable);
    assertThat(page.getContent().get(0)).isEqualTo(expected);
  }

  @Test
  void testFindOneNotFound() {
    var publisherService = new PublisherServiceImpl(mock(PublisherRepository.class),
        mock(PublisherMapper.class), mock(TitleRepository.class));

    var publisherOpt = publisherService.findOne(ID);
    assertThat(publisherOpt).isNotPresent();
  }

  @Test
  void testFindOne() {
    var publisher = new Publisher();
    publisher.setId(ID);
    var publisherRepository = mock(PublisherRepository.class);
    when(publisherRepository.findById(publisher.getId())).thenReturn(Optional.of(publisher));

    var expected = new PublisherDto();
    var publisherMapper = mock(PublisherMapper.class);
    when(publisherMapper.toDto(any(Publisher.class))).thenReturn(expected);

    var publisherService =
        new PublisherServiceImpl(publisherRepository, publisherMapper, mock(TitleRepository.class));

    var publisherOpt = publisherService.findOne(ID);
    assertThat(publisherOpt).isPresent();
    var actual = publisherOpt.get();
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void testDelete() {
    var publisherRepository = mock(PublisherRepository.class);
    var publisherService = new PublisherServiceImpl(publisherRepository,
        mock(PublisherMapper.class), mock(TitleRepository.class));

    publisherService.delete(ID);
    verify(publisherRepository).deleteById(ID);
  }
}
