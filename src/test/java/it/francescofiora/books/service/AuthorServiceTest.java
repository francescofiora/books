package it.francescofiora.books.service;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.books.domain.Author;
import it.francescofiora.books.domain.Title;
import it.francescofiora.books.repository.AuthorRepository;
import it.francescofiora.books.service.dto.AuthorDto;
import it.francescofiora.books.service.dto.NewAuthorDto;
import it.francescofiora.books.service.dto.TitleDto;
import it.francescofiora.books.service.impl.AuthorServiceImpl;
import it.francescofiora.books.service.mapper.AuthorMapper;
import it.francescofiora.books.service.mapper.NewAuthorMapper;
import it.francescofiora.books.service.mapper.TitleMapper;
import it.francescofiora.books.web.errors.NotFoundAlertException;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class AuthorServiceTest {

  static final Long ID = 1L;

  @MockBean
  private AuthorRepository authorRepository;

  @MockBean
  private AuthorMapper authorMapper;

  @MockBean
  private NewAuthorMapper newAuthorMapper;

  @MockBean
  private TitleMapper titleMapper;

  private AuthorService authorService;

  @BeforeEach
  public void setUp() {
    authorService = new AuthorServiceImpl(authorRepository, authorMapper, newAuthorMapper,
        titleMapper);
  }

  @Test
  public void testCreate() throws Exception {
    Author author = new Author();
    Mockito.when(newAuthorMapper.toEntity(Mockito.any(NewAuthorDto.class))).thenReturn(author);
    Mockito.when(authorRepository.save(Mockito.any(Author.class))).thenReturn(author);

    AuthorDto expected = new AuthorDto();
    Mockito.when(authorMapper.toDto(Mockito.any(Author.class))).thenReturn(expected);

    NewAuthorDto authorDto = new NewAuthorDto();
    AuthorDto actual = authorService.create(authorDto);
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void testUpdateNotFound() throws Exception {
    AuthorDto authorDto = new AuthorDto();
    Assertions.assertThrows(NotFoundAlertException.class, () -> authorService.update(authorDto));
  }

  @Test
  public void testUpdate() throws Exception {
    Author author = new Author();
    Mockito.when(authorRepository.findById(Mockito.eq(ID))).thenReturn(Optional.of(author));

    AuthorDto authorDto = new AuthorDto();
    authorDto.setId(ID);
    authorService.update(authorDto);
  }

  @Test
  public void testFindAll() throws Exception {
    Author author = new Author();
    Mockito.when(authorRepository.findAll(Mockito.any(Pageable.class)))
        .thenReturn(new PageImpl<Author>(Collections.singletonList(author)));
    AuthorDto expected = new AuthorDto();
    Mockito.when(authorMapper.toDto(Mockito.any(Author.class))).thenReturn(expected);
    Pageable pageable = PageRequest.of(1, 1);
    Page<AuthorDto> page = authorService.findAll(pageable);
    assertThat(page.getContent().get(0)).isEqualTo(expected);
  }

  @Test
  public void testFindOneNotFound() throws Exception {
    Optional<AuthorDto> authorOpt = authorService.findOne(ID);
    assertThat(authorOpt).isNotPresent();
  }

  @Test
  public void testFindOne() throws Exception {
    Author author = new Author();
    author.setId(ID);
    Mockito.when(authorRepository.findById(Mockito.eq(author.getId())))
        .thenReturn(Optional.of(author));
    AuthorDto expected = new AuthorDto();
    Mockito.when(authorMapper.toDto(Mockito.any(Author.class))).thenReturn(expected);

    Optional<AuthorDto> authorOpt = authorService.findOne(ID);
    assertThat(authorOpt).isPresent();
    AuthorDto actual = authorOpt.get();
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void testFindTitlesByAuthorId() throws Exception {
    Author author = new Author();
    author.getTitles().add(new Title());
    author.setId(ID);
    Mockito.when(authorRepository.findById(Mockito.eq(author.getId())))
        .thenReturn(Optional.of(author));

    TitleDto expected = new TitleDto();
    Mockito.when(titleMapper.toDto(Mockito.anyList()))
        .thenReturn(Collections.singletonList(expected));
    Pageable pageable = PageRequest.of(1, 1);
    Page<TitleDto> page = authorService.findTitlesByAuthorId(pageable, ID);
    assertThat(page.getContent().get(0)).isEqualTo(expected);
  }

  @Test
  public void testDelete() throws Exception {
    authorService.delete(1L);
  }
}
