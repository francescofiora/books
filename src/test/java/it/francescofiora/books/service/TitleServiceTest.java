package it.francescofiora.books.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import it.francescofiora.books.domain.Author;
import it.francescofiora.books.domain.Publisher;
import it.francescofiora.books.domain.Title;
import it.francescofiora.books.repository.AuthorRepository;
import it.francescofiora.books.repository.PublisherRepository;
import it.francescofiora.books.repository.TitleRepository;
import it.francescofiora.books.service.dto.NewTitleDto;
import it.francescofiora.books.service.dto.TitleDto;
import it.francescofiora.books.service.dto.UpdatebleTitleDto;
import it.francescofiora.books.service.impl.TitleServiceImpl;
import it.francescofiora.books.service.mapper.TitleMapper;
import it.francescofiora.books.util.TestUtils;
import it.francescofiora.books.web.errors.NotFoundAlertException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

class TitleServiceTest {

  private static final Long ID = 1L;

  @Test
  void testCreate() {
    var title = new Title();
    var titleMapper = mock(TitleMapper.class);
    when(titleMapper.toEntity(any(NewTitleDto.class))).thenReturn(title);

    var titleRepository = mock(TitleRepository.class);
    when(titleRepository.save(any(Title.class))).thenReturn(title);

    var expected = new TitleDto();
    when(titleMapper.toDto(any(Title.class))).thenReturn(expected);

    var titleDto = TestUtils.createNewTitleDto();
    var authorRepository = mock(AuthorRepository.class);
    when(authorRepository.findById(titleDto.getAuthors().get(0).getId()))
        .thenReturn(Optional.of(new Author()));

    var publisherRepository = mock(PublisherRepository.class);
    when(publisherRepository.findById(titleDto.getPublisher().getId()))
        .thenReturn(Optional.of(new Publisher()));

    var titleService =
        new TitleServiceImpl(titleRepository, titleMapper, authorRepository, publisherRepository);

    var actual = titleService.create(titleDto);
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void testUpdateNotFound() {
    var titleDto = new UpdatebleTitleDto();
    var titleService = new TitleServiceImpl(mock(TitleRepository.class), mock(TitleMapper.class),
        mock(AuthorRepository.class), mock(PublisherRepository.class));

    assertThrows(NotFoundAlertException.class, () -> titleService.update(titleDto));
  }

  @Test
  void testUpdate() {
    var title = new Title();
    var titleRepository = mock(TitleRepository.class);
    when(titleRepository.findById(ID)).thenReturn(Optional.of(title));

    var titleDto = TestUtils.createUpdatebleTitleDto(ID);
    var authorRepository = mock(AuthorRepository.class);
    when(authorRepository.findById(titleDto.getAuthors().get(0).getId()))
        .thenReturn(Optional.of(new Author()));

    var publisherRepository = mock(PublisherRepository.class);
    when(publisherRepository.findById(titleDto.getPublisher().getId()))
        .thenReturn(Optional.of(new Publisher()));

    var titleMapper = mock(TitleMapper.class);
    var titleService =
        new TitleServiceImpl(titleRepository, titleMapper, authorRepository, publisherRepository);

    titleService.update(titleDto);
    verify(titleMapper).updateEntityFromDto(titleDto, title);
    verify(titleRepository).save(title);
  }

  @Test
  void testFindAll() {
    var title = new Title();
    var titleRepository = mock(TitleRepository.class);
    when(titleRepository.findAll(ArgumentMatchers.<Example<Title>>any(), any(Pageable.class)))
        .thenReturn(new PageImpl<Title>(List.of(title)));

    var expected = new TitleDto();
    var titleMapper = mock(TitleMapper.class);
    when(titleMapper.toDto(any(Title.class))).thenReturn(expected);

    var pageable = PageRequest.of(1, 1);
    var titleService = new TitleServiceImpl(titleRepository, titleMapper,
        mock(AuthorRepository.class), mock(PublisherRepository.class));

    var page = titleService.findAll(null, pageable);
    assertThat(page.getContent().get(0)).isEqualTo(expected);
  }

  @Test
  void testFindOneNotFound() {
    var titleService = new TitleServiceImpl(mock(TitleRepository.class), mock(TitleMapper.class),
        mock(AuthorRepository.class), mock(PublisherRepository.class));
    var titleOpt = titleService.findOne(ID);
    assertThat(titleOpt).isNotPresent();
  }

  @Test
  void testFindOne() {
    var title = new Title();
    title.setId(ID);
    var titleRepository = mock(TitleRepository.class);
    when(titleRepository.findById(title.getId())).thenReturn(Optional.of(title));

    var expected = new TitleDto();
    var titleMapper = mock(TitleMapper.class);
    when(titleMapper.toDto(any(Title.class))).thenReturn(expected);

    var titleService = new TitleServiceImpl(titleRepository, titleMapper,
        mock(AuthorRepository.class), mock(PublisherRepository.class));

    var titleOpt = titleService.findOne(ID);
    assertThat(titleOpt).isPresent();
    var actual = titleOpt.get();
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void testDelete() {
    var titleRepository = mock(TitleRepository.class);
    var titleService = new TitleServiceImpl(titleRepository, mock(TitleMapper.class),
        mock(AuthorRepository.class), mock(PublisherRepository.class));
    titleService.delete(ID);
    verify(titleRepository).deleteById(ID);
  }
}
