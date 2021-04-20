package it.francescofiora.books.service;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
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
public class AuthorServiceTest {

  private static final Long ID = 1L;

  @MockBean
  private AuthorRepository authorRepository;

  @MockBean
  private AuthorMapper authorMapper;

  @MockBean
  private TitleMapper titleMapper;

  private AuthorService authorService;

  @BeforeEach
  void setUp() {
    authorService =
        new AuthorServiceImpl(authorRepository, authorMapper, titleMapper);
  }

  @Test
  void testCreate() throws Exception {
    Author author = new Author();
    when(authorMapper.toEntity(any(NewAuthorDto.class))).thenReturn(author);
    when(authorRepository.save(any(Author.class))).thenReturn(author);

    AuthorDto expected = new AuthorDto();
    when(authorMapper.toDto(any(Author.class))).thenReturn(expected);

    NewAuthorDto authorDto = new NewAuthorDto();
    AuthorDto actual = authorService.create(authorDto);
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void testUpdateNotFound() throws Exception {
    AuthorDto authorDto = new AuthorDto();
    assertThrows(NotFoundAlertException.class, () -> authorService.update(authorDto));
  }

  @Test
  void testUpdate() throws Exception {
    Author author = new Author();
    when(authorRepository.findById(eq(ID))).thenReturn(Optional.of(author));

    AuthorDto authorDto = new AuthorDto();
    authorDto.setId(ID);
    authorService.update(authorDto);
  }

  @Test
  void testFindAll() throws Exception {
    Author author = new Author();
    when(authorRepository.findAll(any(Pageable.class)))
        .thenReturn(new PageImpl<Author>(singletonList(author)));
    AuthorDto expected = new AuthorDto();
    when(authorMapper.toDto(any(Author.class))).thenReturn(expected);
    Pageable pageable = PageRequest.of(1, 1);
    Page<AuthorDto> page = authorService.findAll(pageable);
    assertThat(page.getContent().get(0)).isEqualTo(expected);
  }

  @Test
  void testFindOneNotFound() throws Exception {
    Optional<AuthorDto> authorOpt = authorService.findOne(ID);
    assertThat(authorOpt).isNotPresent();
  }

  @Test
  void testFindOne() throws Exception {
    Author author = new Author();
    author.setId(ID);
    when(authorRepository.findById(eq(author.getId()))).thenReturn(Optional.of(author));
    AuthorDto expected = new AuthorDto();
    when(authorMapper.toDto(any(Author.class))).thenReturn(expected);

    Optional<AuthorDto> authorOpt = authorService.findOne(ID);
    assertThat(authorOpt).isPresent();
    AuthorDto actual = authorOpt.get();
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void testFindTitlesByAuthorId() throws Exception {
    Author author = new Author();
    author.getTitles().add(new Title());
    author.setId(ID);
    when(authorRepository.findById(eq(author.getId()))).thenReturn(Optional.of(author));

    TitleDto expected = new TitleDto();
    when(titleMapper.toDto(anyList())).thenReturn(singletonList(expected));
    Pageable pageable = PageRequest.of(1, 1);
    Page<TitleDto> page = authorService.findTitlesByAuthorId(pageable, ID);
    assertThat(page.getContent().get(0)).isEqualTo(expected);
  }

  @Test
  void testDelete() throws Exception {
    authorService.delete(ID);
  }
}
