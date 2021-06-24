package it.francescofiora.books.service;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import it.francescofiora.books.domain.Publisher;
import it.francescofiora.books.repository.PublisherRepository;
import it.francescofiora.books.repository.TitleRepository;
import it.francescofiora.books.service.dto.NewPublisherDto;
import it.francescofiora.books.service.dto.PublisherDto;
import it.francescofiora.books.service.impl.PublisherServiceImpl;
import it.francescofiora.books.service.mapper.PublisherMapper;
import it.francescofiora.books.web.errors.NotFoundAlertException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class PublisherServiceTest {

  private static final Long ID = 1L;

  @MockBean
  private PublisherRepository publisherRepository;

  @MockBean
  private TitleRepository titleRepository;

  @MockBean
  private PublisherMapper publisherMapper;

  private PublisherService publisherService;

  @BeforeEach
  void setUp() {
    publisherService =
        new PublisherServiceImpl(publisherRepository, publisherMapper, titleRepository);
  }

  @Test
  void testCreate() throws Exception {
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
  void testUpdateNotFound() throws Exception {
    var publisherDto = new PublisherDto();
    assertThrows(NotFoundAlertException.class, () -> publisherService.update(publisherDto));
  }

  @Test
  void testUpdate() throws Exception {
    var publisher = new Publisher();
    when(publisherRepository.findById(eq(ID))).thenReturn(Optional.of(publisher));

    var publisherDto = new PublisherDto();
    publisherDto.setId(ID);
    publisherService.update(publisherDto);
  }

  @Test
  void testFindAll() throws Exception {
    var publisher = new Publisher();
    when(publisherRepository.findAll(any(Pageable.class)))
        .thenReturn(new PageImpl<Publisher>(singletonList(publisher)));
    var expected = new PublisherDto();
    when(publisherMapper.toDto(any(Publisher.class))).thenReturn(expected);
    var pageable = PageRequest.of(1, 1);
    var page = publisherService.findAll(pageable);
    assertThat(page.getContent().get(0)).isEqualTo(expected);
  }

  @Test
  void testFindOneNotFound() throws Exception {
    var publisherOpt = publisherService.findOne(ID);
    assertThat(publisherOpt).isNotPresent();
  }

  @Test
  void testFindOne() throws Exception {
    var publisher = new Publisher();
    publisher.setId(ID);
    when(publisherRepository.findById(eq(publisher.getId()))).thenReturn(Optional.of(publisher));
    var expected = new PublisherDto();
    when(publisherMapper.toDto(any(Publisher.class))).thenReturn(expected);

    var publisherOpt = publisherService.findOne(ID);
    assertThat(publisherOpt).isPresent();
    var actual = publisherOpt.get();
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void testDelete() throws Exception {
    publisherService.delete(ID);
  }
}
