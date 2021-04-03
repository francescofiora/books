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
import it.francescofiora.books.service.mapper.NewPublisherMapper;
import it.francescofiora.books.service.mapper.PublisherMapper;
import it.francescofiora.books.web.errors.NotFoundAlertException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class PublisherServiceTest {

  private static final Long ID = 1L;

  @MockBean
  private PublisherRepository publisherRepository;

  @MockBean
  private TitleRepository titleRepository;

  @MockBean
  private PublisherMapper publisherMapper;

  @MockBean
  private NewPublisherMapper newPublisherMapper;

  private PublisherService publisherService;

  @BeforeEach
  public void setUp() {
    publisherService = new PublisherServiceImpl(publisherRepository, publisherMapper,
        newPublisherMapper, titleRepository);
  }

  @Test
  public void testCreate() throws Exception {
    Publisher publisher = new Publisher();
    when(newPublisherMapper.toEntity(any(NewPublisherDto.class))).thenReturn(publisher);
    when(publisherRepository.save(any(Publisher.class))).thenReturn(publisher);

    PublisherDto expected = new PublisherDto();
    when(publisherMapper.toDto(any(Publisher.class))).thenReturn(expected);

    NewPublisherDto publisherDto = new NewPublisherDto();
    PublisherDto actual = publisherService.create(publisherDto);
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void testUpdateNotFound() throws Exception {
    PublisherDto publisherDto = new PublisherDto();
    assertThrows(NotFoundAlertException.class, () -> publisherService.update(publisherDto));
  }

  @Test
  public void testUpdate() throws Exception {
    Publisher publisher = new Publisher();
    when(publisherRepository.findById(eq(ID))).thenReturn(Optional.of(publisher));

    PublisherDto publisherDto = new PublisherDto();
    publisherDto.setId(ID);
    publisherService.update(publisherDto);
  }

  @Test
  public void testFindAll() throws Exception {
    Publisher publisher = new Publisher();
    when(publisherRepository.findAll(any(Pageable.class)))
        .thenReturn(new PageImpl<Publisher>(singletonList(publisher)));
    PublisherDto expected = new PublisherDto();
    when(publisherMapper.toDto(any(Publisher.class))).thenReturn(expected);
    Pageable pageable = PageRequest.of(1, 1);
    Page<PublisherDto> page = publisherService.findAll(pageable);
    assertThat(page.getContent().get(0)).isEqualTo(expected);
  }

  @Test
  public void testFindOneNotFound() throws Exception {
    Optional<PublisherDto> publisherOpt = publisherService.findOne(ID);
    assertThat(publisherOpt).isNotPresent();
  }

  @Test
  public void testFindOne() throws Exception {
    Publisher publisher = new Publisher();
    publisher.setId(ID);
    when(publisherRepository.findById(eq(publisher.getId()))).thenReturn(Optional.of(publisher));
    PublisherDto expected = new PublisherDto();
    when(publisherMapper.toDto(any(Publisher.class))).thenReturn(expected);

    Optional<PublisherDto> publisherOpt = publisherService.findOne(ID);
    assertThat(publisherOpt).isPresent();
    PublisherDto actual = publisherOpt.get();
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void testDelete() throws Exception {
    publisherService.delete(ID);
  }
}
