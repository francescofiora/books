package it.francescofiora.books.repository;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource(locations = { "classpath:application_test.properties" })
public abstract class AbstractTestRepository {

  @Autowired
  private TestEntityManager entityManager;

  public TestEntityManager getEntityManager() {
    return entityManager;
  }

}
