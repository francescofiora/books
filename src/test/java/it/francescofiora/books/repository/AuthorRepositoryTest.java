package it.francescofiora.books.repository;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.books.domain.Author;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public class AuthorRepositoryTest extends AbstractTestRepository {

  @Autowired
  private AuthorRepository authorRepository;

  @Test
  public void testCrud() throws Exception {
    Author expected1 = UtilsRepository.createAuthor1();
    Author expected2 = UtilsRepository.createAuthor2();
    authorRepository.save(expected1);
    authorRepository.save(expected2);

    Page<Author> authors = authorRepository.findAll(PageRequest.of(0, 10));
    assertThat(authors).isNotNull().isNotEmpty();

    for (Author actual : authors) {
      assertThat(actual).isNotNull();
      assertThat(UtilsRepository.dataEquals(expected1, actual)
          || UtilsRepository.dataEquals(expected2, actual)).isTrue();
    }

    Author expected3 = UtilsRepository.createAuthor3();
    Author author = authors.getContent().get(0);
    author.setFirstName(expected3.getFirstName());
    author.setLastName(expected3.getLastName());
    authorRepository.save(author);

    Optional<Author> optional = authorRepository.findById(author.getId());
    assertThat(optional).isPresent();
    author = optional.get();
    assertThat(UtilsRepository.dataEquals(expected3, author)).isTrue();

    for (Author actual : authors) {
      authorRepository.delete(actual);
    }

    authors = authorRepository.findAll(PageRequest.of(0, 10));
    assertThat(authors).isNotNull().isEmpty();
  }

}
