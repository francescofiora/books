package it.francescofiora.books.repository;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.books.domain.Publisher;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public class PublisherRepositoryTest extends AbstractTestRepository {

  @Autowired
  private PublisherRepository publisherRepository;

  @Test
  public void testCrud() throws Exception {
    Publisher expected1 = UtilsRepository.createPublisher1();
    Publisher expected2 = UtilsRepository.createPublisher2();
    publisherRepository.save(expected1);
    publisherRepository.save(expected2);

    Page<Publisher> publishers = publisherRepository.findAll(PageRequest.of(0, 10));
    assertThat(publishers).isNotNull().isNotEmpty();
    for (Publisher actual : publishers) {
      assertThat(actual).isNotNull();
      assertThat(UtilsRepository.dataEquals(expected1, actual)
          || UtilsRepository.dataEquals(expected2, actual)).isTrue();
    }

    Publisher expected3 = UtilsRepository.createPublisher3();
    Publisher publisher = publishers.getContent().get(0);
    publisher.setPublisherName(expected3.getPublisherName());
    publisherRepository.save(publisher);

    Optional<Publisher> optional = publisherRepository.findById(publisher.getId());
    assertThat(optional).isPresent();
    publisher = optional.get();
    assertThat(UtilsRepository.dataEquals(expected3, publisher)).isTrue();

    for (Publisher actual : publishers) {
      publisherRepository.delete(actual);
    }

    publishers = publisherRepository.findAll(PageRequest.of(0, 10));
    assertThat(publishers).isNotNull().isEmpty();
  }
}
