package it.francescofiora.books.repository;

import org.junit.Assert;

import java.util.List;
import java.util.Optional;

import org.junit.Test;
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
    Assert.assertNotNull(authors);
    Assert.assertFalse(authors.isEmpty());
    for (Author actual : authors) {
      Assert.assertNotNull(actual);
      Assert.assertTrue(UtilsRepository.assertEquals(expecteted1, actual)
          || UtilsRepository.assertEquals(expecteted2, actual));
    }

    Author expecteted3 = UtilsRepository.createAuthor3();
    Author author = authors.getContent().get(0);
    author.setFirstName(expecteted3.getFirstName());
    author.setLastName(expecteted3.getLastName());
    authorRepository.save(author);

    Optional<Author> optional = authorRepository.findById(author.getId());
    Assert.assertTrue(optional.isPresent());
    author = optional.get();
    Assert.assertTrue(UtilsRepository.assertEquals(expecteted3, author));

    for (Author actual : authors) {
      authorRepository.delete(actual);
    }

    authors = authorRepository.findAll(PageRequest.of(0, 10));
    Assert.assertNotNull(authors);
    Assert.assertTrue(authors.isEmpty());
  }

}
