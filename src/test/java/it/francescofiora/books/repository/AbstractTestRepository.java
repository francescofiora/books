package it.francescofiora.books.repository;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Abstract Test for Repository tests.
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest
@TestPropertySource(locations = { "classpath:application_test.properties" })
public abstract class AbstractTestRepository {

  @Autowired
  private TestEntityManager entityManager;

  protected TestEntityManager getEntityManager() {
    return entityManager;
  }
}
