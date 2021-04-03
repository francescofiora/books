package it.francescofiora.books.service;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import it.francescofiora.books.domain.Title;
import it.francescofiora.books.repository.TitleRepository;
import it.francescofiora.books.service.dto.NewTitleDto;
import it.francescofiora.books.service.dto.TitleDto;
import it.francescofiora.books.service.dto.UpdatebleTitleDto;
import it.francescofiora.books.service.impl.TitleServiceImpl;
import it.francescofiora.books.service.mapper.NewTitleMapper;
import it.francescofiora.books.service.mapper.TitleMapper;
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
public class TitleServiceTest {

  private static final Long ID = 1L;
  
  @MockBean
  private TitleRepository titleRepository;

  @MockBean
  private TitleMapper titleMapper;

  @MockBean
  private NewTitleMapper newTitleMapper;

  private TitleService titleService;

  @BeforeEach
  public void setUp() {
    titleService = new TitleServiceImpl(titleRepository, titleMapper, newTitleMapper);
  }

  @Test
  public void testCreate() throws Exception {
    Title title = new Title();
    when(newTitleMapper.toEntity(any(NewTitleDto.class))).thenReturn(title);
    when(titleRepository.save(any(Title.class))).thenReturn(title);

    TitleDto expected = new TitleDto();
    when(titleMapper.toDto(any(Title.class))).thenReturn(expected);

    NewTitleDto titleDto = new NewTitleDto();
    TitleDto actual = titleService.create(titleDto);
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void testUpdateNotFound() throws Exception {
    UpdatebleTitleDto titleDto = new UpdatebleTitleDto();
    assertThrows(NotFoundAlertException.class, () -> titleService.update(titleDto));
  }

  @Test
  public void testUpdate() throws Exception {
    Title title = new Title();
    when(titleRepository.findById(eq(ID))).thenReturn(Optional.of(title));

    UpdatebleTitleDto titleDto = new UpdatebleTitleDto();
    titleDto.setId(ID);
    titleService.update(titleDto);
  }

  @Test
  public void testFindAll() throws Exception {
    Title title = new Title();
    when(titleRepository.findAll(any(Pageable.class)))
        .thenReturn(new PageImpl<Title>(singletonList(title)));
    TitleDto expected = new TitleDto();
    when(titleMapper.toDto(any(Title.class))).thenReturn(expected);
    Pageable pageable = PageRequest.of(1, 1);
    Page<TitleDto> page = titleService.findAll(pageable);
    assertThat(page.getContent().get(0)).isEqualTo(expected);
  }

  @Test
  public void testFindOneNotFound() throws Exception {
    Optional<TitleDto> titleOpt = titleService.findOne(ID);
    assertThat(titleOpt).isNotPresent();
  }

  @Test
  public void testFindOne() throws Exception {
    Title title = new Title();
    title.setId(ID);
    when(titleRepository.findById(eq(title.getId())))
        .thenReturn(Optional.of(title));
    TitleDto expected = new TitleDto();
    when(titleMapper.toDto(any(Title.class))).thenReturn(expected);

    Optional<TitleDto> titleOpt = titleService.findOne(ID);
    assertThat(titleOpt).isPresent();
    TitleDto actual = titleOpt.get();
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void testDelete() throws Exception {
    titleService.delete(ID);
  }

}
