package it.francescofiora.books.repository;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.books.util.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

class TitleRepositoryTest extends AbstractTestRepository {

  @Autowired
  private TitleRepository titleRepository;

  @Test
  void testCrud() {
    var expected1 = TestUtils.createTitle1();
    expected1.getAuthors().forEach(author -> getEntityManager().persist(author));
    getEntityManager().persist(expected1.getPublisher());
    titleRepository.save(expected1);

    var expected2 = TestUtils.createTitle2();
    expected2.getAuthors().forEach(author -> getEntityManager().persist(author));
    getEntityManager().persist(expected2.getPublisher());
    titleRepository.save(expected2);

    var titles = titleRepository.findAll(PageRequest.of(0, 10));
    assertThat(titles).isNotNull().isNotNull();
    for (var actual : titles) {
      assertThat(actual).isNotNull();
      assertThat(TestUtils.dataEquals(expected1, actual) || TestUtils.dataEquals(expected2, actual))
          .isTrue();
    }

    var expected3 = TestUtils.createTitle3();
    var title = titles.getContent().get(0);
    title.setCopyright(expected3.getCopyright());
    title.setEditionNumber(expected3.getEditionNumber());
    title.setImageFile(expected3.getImageFile());
    title.setName(expected3.getName());
    title.setLanguage(expected3.getLanguage());
    title.setPrice(expected3.getPrice());
    titleRepository.save(title);

    var optional = titleRepository.findById(title.getId());
    assertThat(optional).isPresent();
    title = optional.get();
    assertThat(TestUtils.dataEquals(expected3, title)).isTrue();

    for (var actual : titles) {
      titleRepository.delete(actual);
    }

    titles = titleRepository.findAll(PageRequest.of(0, 10));
    assertThat(titles).isNotNull().isEmpty();
  }
}
