package it.francescofiora.books.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import it.francescofiora.books.domain.Author;
import it.francescofiora.books.domain.Title;
import it.francescofiora.books.repository.AuthorRepository;
import it.francescofiora.books.service.dto.AuthorDto;
import it.francescofiora.books.service.dto.NewAuthorDto;
import it.francescofiora.books.service.dto.TitleDto;
import it.francescofiora.books.service.impl.AuthorServiceImpl;
import it.francescofiora.books.service.mapper.AuthorMapper;
import it.francescofiora.books.service.mapper.TitleMapper;
import it.francescofiora.books.web.errors.NotFoundAlertException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

class AuthorServiceTest {

  private static final Long ID = 1L;

  @Test
  void testCreate() {
    var authorMapper = mock(AuthorMapper.class);
    var authorRepository = mock(AuthorRepository.class);
    var authorService =
        new AuthorServiceImpl(authorRepository, authorMapper, mock(TitleMapper.class));
    var author = new Author();
    when(authorMapper.toEntity(any(NewAuthorDto.class))).thenReturn(author);
    when(authorRepository.save(any(Author.class))).thenReturn(author);

    var expected = new AuthorDto();
    when(authorMapper.toDto(any(Author.class))).thenReturn(expected);

    var authorDto = new NewAuthorDto();
    var actual = authorService.create(authorDto);
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void testUpdateNotFound() {
    var authorService = new AuthorServiceImpl(mock(AuthorRepository.class),
        mock(AuthorMapper.class), mock(TitleMapper.class));
    var authorDto = new AuthorDto();
    assertThrows(NotFoundAlertException.class, () -> authorService.update(authorDto));
  }

  @Test
  void testUpdate() {
    var authorMapper = mock(AuthorMapper.class);
    var authorRepository = mock(AuthorRepository.class);
    var authorService =
        new AuthorServiceImpl(authorRepository, authorMapper, mock(TitleMapper.class));
    var author = new Author();
    when(authorRepository.findById(ID)).thenReturn(Optional.of(author));

    var authorDto = new AuthorDto();
    authorDto.setId(ID);
    authorService.update(authorDto);
    verify(authorMapper).updateEntityFromDto(authorDto, author);
    verify(authorRepository).save(author);
  }

  @Test
  void testFindAll() {
    var author = new Author();
    var authorRepository = mock(AuthorRepository.class);
    when(authorRepository.findAll(ArgumentMatchers.<Example<Author>>any(), any(Pageable.class)))
        .thenReturn(new PageImpl<>(List.of(author)));

    var expected = new AuthorDto();
    var authorMapper = mock(AuthorMapper.class);
    when(authorMapper.toDto(any(Author.class))).thenReturn(expected);

    var pageable = PageRequest.of(1, 1);
    var authorService =
        new AuthorServiceImpl(authorRepository, authorMapper, mock(TitleMapper.class));
    var page = authorService.findAll(null, null, pageable);
    assertThat(page.getContent().get(0)).isEqualTo(expected);
  }

  @Test
  void testFindOneNotFound() {
    var authorService = new AuthorServiceImpl(mock(AuthorRepository.class),
        mock(AuthorMapper.class), mock(TitleMapper.class));
    var authorOpt = authorService.findOne(ID);
    assertThat(authorOpt).isNotPresent();
  }

  @Test
  void testFindOne() {
    var authorRepository = mock(AuthorRepository.class);
    var author = new Author();
    author.setId(ID);
    when(authorRepository.findById(author.getId())).thenReturn(Optional.of(author));

    var expected = new AuthorDto();
    var authorMapper = mock(AuthorMapper.class);
    when(authorMapper.toDto(any(Author.class))).thenReturn(expected);

    var authorService =
        new AuthorServiceImpl(authorRepository, authorMapper, mock(TitleMapper.class));

    var authorOpt = authorService.findOne(ID);
    assertThat(authorOpt).isPresent();
    var actual = authorOpt.get();
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void testFindTitlesByAuthorId() {
    var author = new Author();
    author.getTitles().add(new Title());
    author.setId(ID);
    var authorRepository = mock(AuthorRepository.class);
    when(authorRepository.findById(author.getId())).thenReturn(Optional.of(author));

    var expected = new TitleDto();
    var titleMapper = mock(TitleMapper.class);
    when(titleMapper.toDto(anyList())).thenReturn(List.of(expected));
    var pageable = PageRequest.of(1, 1);

    var authorService =
        new AuthorServiceImpl(authorRepository, mock(AuthorMapper.class), titleMapper);

    var page = authorService.findTitlesByAuthorId(pageable, ID);
    assertThat(page.getContent().get(0)).isEqualTo(expected);
  }

  @Test
  void testDelete() {
    var authorRepository = mock(AuthorRepository.class);
    var authorService =
        new AuthorServiceImpl(authorRepository, mock(AuthorMapper.class), mock(TitleMapper.class));
    authorService.delete(ID);
    verify(authorRepository).deleteById(ID);
  }
}
