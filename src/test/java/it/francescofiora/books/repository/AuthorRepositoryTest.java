package it.francescofiora.books.repository;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.books.util.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

class AuthorRepositoryTest extends AbstractTestRepository {

  @Autowired
  private AuthorRepository authorRepository;

  @Test
  void testCrud() throws Exception {
    var expected1 = TestUtils.createAuthor1();
    var expected2 = TestUtils.createAuthor2();
    authorRepository.save(expected1);
    authorRepository.save(expected2);

    var authors = authorRepository.findAll(PageRequest.of(0, 10));
    assertThat(authors).isNotNull().isNotEmpty();

    for (var actual : authors) {
      assertThat(actual).isNotNull();
      assertThat(TestUtils.dataEquals(expected1, actual)
          || TestUtils.dataEquals(expected2, actual)).isTrue();
    }

    var expected3 = TestUtils.createAuthor3();
    var author = authors.getContent().get(0);
    author.setFirstName(expected3.getFirstName());
    author.setLastName(expected3.getLastName());
    authorRepository.save(author);

    var optional = authorRepository.findById(author.getId());
    assertThat(optional).isPresent();
    author = optional.get();
    assertThat(TestUtils.dataEquals(expected3, author)).isTrue();

    for (var actual : authors) {
      authorRepository.delete(actual);
    }

    authors = authorRepository.findAll(PageRequest.of(0, 10));
    assertThat(authors).isNotNull().isEmpty();
  }

}
