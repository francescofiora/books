package it.francescofiora.books.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import it.francescofiora.books.domain.Author;

public class AuthorRepositoryTest extends AbstractTestRepository {

  @Autowired
  private AuthorRepository authorRepository;

  @Test
  public void testCRUD() throws Exception {
    Author expecteted1 = UtilsRepository.createAuthor1();
    Author expecteted2 = UtilsRepository.createAuthor2();
    authorRepository.save(expecteted1);
    authorRepository.save(expecteted2);

    Page<Author> authors = authorRepository.findAll(PageRequest.of(0, 10));
    assertThat(authors).isNotNull().isNotEmpty();

    for (Author actual : authors) {
      assertThat(actual).isNotNull();
      assertThat(UtilsRepository.assertEquals(expecteted1, actual)
          || UtilsRepository.assertEquals(expecteted2, actual)).isTrue();
    }

    Author expecteted3 = UtilsRepository.createAuthor3();
    Author author = authors.getContent().get(0);
    author.setFirstName(expecteted3.getFirstName());
    author.setLastName(expecteted3.getLastName());
    authorRepository.save(author);

    Optional<Author> optional = authorRepository.findById(author.getId());
    assertThat(optional).isPresent();
    author = optional.get();
    assertThat(UtilsRepository.assertEquals(expecteted3, author)).isTrue();

    for (Author actual : authors) {
      authorRepository.delete(actual);
    }

    authors = authorRepository.findAll(PageRequest.of(0, 10));
    assertThat(authors).isNotNull().isEmpty();
  }

}
