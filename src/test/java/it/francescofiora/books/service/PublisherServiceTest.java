package it.francescofiora.books.service;

import it.francescofiora.books.domain.Publisher;
import it.francescofiora.books.repository.PublisherRepository;
import it.francescofiora.books.service.dto.PublisherDto;
import it.francescofiora.books.service.dto.NewPublisherDto;
import it.francescofiora.books.service.impl.PublisherServiceImpl;
import it.francescofiora.books.service.mapper.PublisherMapper;
import it.francescofiora.books.service.mapper.NewPublisherMapper;
import it.francescofiora.books.web.errors.NotFoundAlertException;

import java.util.Collections;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class PublisherServiceTest {

  @MockBean
  private PublisherRepository publisherRepository;

  @MockBean
  private PublisherMapper publisherMapper;

  @MockBean
  private NewPublisherMapper newPublisherMapper;

  private PublisherService publisherService;

  @Before
  public void setUp() {
    publisherService = new PublisherServiceImpl(publisherRepository, publisherMapper, newPublisherMapper);
  }

  @Test
  public void testCreate() throws Exception {
    Publisher publisher = new Publisher();
    Mockito.when(newPublisherMapper.toEntity(Mockito.any(NewPublisherDto.class))).thenReturn(publisher);
    Mockito.when(publisherRepository.save(Mockito.any(Publisher.class))).thenReturn(publisher);

    PublisherDto expected = new PublisherDto();
    Mockito.when(publisherMapper.toDto(Mockito.any(Publisher.class))).thenReturn(expected);

    NewPublisherDto publisherDto = new NewPublisherDto();
    PublisherDto actual = publisherService.create(publisherDto);
    Assert.assertEquals(expected, actual);
  }

  @Test(expected = NotFoundAlertException.class)
  public void testUpdateNotFound() throws Exception {
    PublisherDto publisherDto = new PublisherDto();
    publisherService.update(publisherDto);
  }

  @Test
  public void testUpdate() throws Exception {
    Publisher publisher = new Publisher();
    Mockito.when(publisherRepository.findById(Mockito.eq(1L))).thenReturn(Optional.of(publisher));

    PublisherDto publisherDto = new PublisherDto();
    publisherDto.setId(1L);
    publisherService.update(publisherDto);
  }

  @Test
  public void testFindAll() throws Exception {
    Publisher publisher = new Publisher();
    Mockito.when(publisherRepository.findAll(Mockito.any(Pageable.class)))
        .thenReturn(new PageImpl<Publisher>(Collections.singletonList(publisher)));
    PublisherDto expected = new PublisherDto();
    Mockito.when(publisherMapper.toDto(Mockito.any(Publisher.class))).thenReturn(expected);
    Pageable pageable = PageRequest.of(1, 1);
    Page<PublisherDto> page = publisherService.findAll(pageable);
    Assert.assertEquals(expected, page.getContent().get(0));
  }

  @Test
  public void testFindOneNotFound() throws Exception {
    Optional<PublisherDto> publisherOpt = publisherService.findOne(1L);
    Assert.assertFalse(publisherOpt.isPresent());
  }

  @Test
  public void testFindOne() throws Exception {
    Publisher publisher = new Publisher();
    publisher.setId(1L);
    Mockito.when(publisherRepository.findById(Mockito.eq(publisher.getId())))
        .thenReturn(Optional.of(publisher));
    PublisherDto expected = new PublisherDto();
    Mockito.when(publisherMapper.toDto(Mockito.any(Publisher.class))).thenReturn(expected);

    Optional<PublisherDto> publisherOpt = publisherService.findOne(1L);
    Assert.assertTrue(publisherOpt.isPresent());
    PublisherDto actual = publisherOpt.get();
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testDelete() throws Exception {
    publisherService.delete(1L);
  }
}