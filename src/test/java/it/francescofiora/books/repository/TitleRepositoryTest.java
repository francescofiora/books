package it.francescofiora.books.repository;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.books.domain.Title;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public class TitleRepositoryTest extends AbstractTestRepository {

  @Autowired
  private TitleRepository titleRepository;

  @Test
  public void testCrud() throws Exception {
    Title expected1 = UtilsRepository.createTitle1();
    expected1.getAuthors().forEach(author -> getEntityManager().persist(author));
    getEntityManager().persist(expected1.getPublisher());
    titleRepository.save(expected1);

    Title expected2 = UtilsRepository.createTitle2();
    expected2.getAuthors().forEach(author -> getEntityManager().persist(author));
    getEntityManager().persist(expected2.getPublisher());
    titleRepository.save(expected2);

    Page<Title> titles = titleRepository.findAll(PageRequest.of(0, 10));
    assertThat(titles).isNotNull().isNotNull();
    for (Title actual : titles) {
      assertThat(actual).isNotNull();
      assertThat(UtilsRepository.dataEquals(expected1, actual)
          || UtilsRepository.dataEquals(expected2, actual)).isTrue();
    }

    Title expected3 = UtilsRepository.createTitle3();
    Title title = titles.getContent().get(0);
    title.setCopyright(expected3.getCopyright());
    title.setEditionNumber(expected3.getEditionNumber());
    title.setImageFile(expected3.getImageFile());
    title.setTitle(expected3.getTitle());
    title.setLanguage(expected3.getLanguage());
    title.setPrice(expected3.getPrice());
    titleRepository.save(title);

    Optional<Title> optional = titleRepository.findById(title.getId());
    assertThat(optional).isPresent();
    title = optional.get();
    assertThat(UtilsRepository.dataEquals(expected3, title)).isTrue();

    for (Title actual : titles) {
      titleRepository.delete(actual);
    }

    titles = titleRepository.findAll(PageRequest.of(0, 10));
    assertThat(titles).isNotNull().isEmpty();
  }
}
