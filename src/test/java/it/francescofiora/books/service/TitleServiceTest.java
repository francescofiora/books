package it.francescofiora.books.service;

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

import it.francescofiora.books.domain.Title;
import it.francescofiora.books.repository.TitleRepository;
import it.francescofiora.books.service.dto.NewTitleDto;
import it.francescofiora.books.service.dto.TitleDto;
import it.francescofiora.books.service.dto.UpdatebleTitleDto;
import it.francescofiora.books.service.impl.TitleServiceImpl;
import it.francescofiora.books.service.mapper.NewTitleMapper;
import it.francescofiora.books.service.mapper.TitleMapper;
import it.francescofiora.books.web.errors.NotFoundAlertException;

@RunWith(SpringRunner.class)
public class TitleServiceTest {

  @MockBean
  private TitleRepository titleRepository;

  @MockBean
  private TitleMapper titleMapper;

  @MockBean
  private NewTitleMapper newTitleMapper;

  private TitleService titleService;

  @Before
  public void setUp() {
    titleService = new TitleServiceImpl(titleRepository, titleMapper, newTitleMapper);
  }

  @Test
  public void testCreate() throws Exception {
    Title title = new Title();
    Mockito.when(newTitleMapper.toEntity(Mockito.any(NewTitleDto.class))).thenReturn(title);
    Mockito.when(titleRepository.save(Mockito.any(Title.class))).thenReturn(title);

    TitleDto expected = new TitleDto();
    Mockito.when(titleMapper.toDto(Mockito.any(Title.class))).thenReturn(expected);

    NewTitleDto titleDto = new NewTitleDto();
    TitleDto actual = titleService.create(titleDto);
    Assert.assertEquals(expected, actual);
  }

  @Test(expected = NotFoundAlertException.class)
  public void testUpdateNotFound() throws Exception {
    UpdatebleTitleDto titleDto = new UpdatebleTitleDto();
    titleService.update(titleDto);
  }

  @Test
  public void testUpdate() throws Exception {
    Title title = new Title();
    Mockito.when(titleRepository.findById(Mockito.eq(1L))).thenReturn(Optional.of(title));

    UpdatebleTitleDto titleDto = new UpdatebleTitleDto();
    titleDto.setId(1L);
    titleService.update(titleDto);
  }

  @Test
  public void testFindAll() throws Exception {
    Title title = new Title();
    Mockito.when(titleRepository.findAll(Mockito.any(Pageable.class)))
        .thenReturn(new PageImpl<Title>(Collections.singletonList(title)));
    TitleDto expected = new TitleDto();
    Mockito.when(titleMapper.toDto(Mockito.any(Title.class))).thenReturn(expected);
    Pageable pageable = PageRequest.of(1, 1);
    Page<TitleDto> page = titleService.findAll(pageable);
    Assert.assertEquals(expected, page.getContent().get(0));
  }

  @Test
  public void testFindOneNotFound() throws Exception {
    Optional<TitleDto> titleOpt = titleService.findOne(1L);
    Assert.assertFalse(titleOpt.isPresent());
  }

  @Test
  public void testFindOne() throws Exception {
    Title title = new Title();
    title.setId(1L);
    Mockito.when(titleRepository.findById(Mockito.eq(title.getId())))
        .thenReturn(Optional.of(title));
    TitleDto expected = new TitleDto();
    Mockito.when(titleMapper.toDto(Mockito.any(Title.class))).thenReturn(expected);

    Optional<TitleDto> titleOpt = titleService.findOne(1L);
    Assert.assertTrue(titleOpt.isPresent());
    TitleDto actual = titleOpt.get();
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testDelete() throws Exception {
    titleService.delete(1L);
  }

}
