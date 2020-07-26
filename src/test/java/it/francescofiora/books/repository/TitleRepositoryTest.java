package it.francescofiora.books.repository;

import it.francescofiora.books.domain.Title;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public class TitleRepositoryTest extends AbstractTestRepository {

  @Autowired
  private TitleRepository titleRepository;

  @Test
  public void testCRUD() throws Exception {
    Title expecteted1 = UtilsRepository.createTitle1();
    expecteted1.getAuthors().forEach(author -> getEntityManager().persist(author));
    getEntityManager().persist(expecteted1.getPublisher());
    titleRepository.save(expecteted1);

    Title expecteted2 = UtilsRepository.createTitle2();
    expecteted2.getAuthors().forEach(author -> getEntityManager().persist(author));
    getEntityManager().persist(expecteted2.getPublisher());
    titleRepository.save(expecteted2);

    Page<Title> titles = titleRepository.findAll(PageRequest.of(0, 10));
    Assert.assertNotNull(titles);
    Assert.assertFalse(titles.isEmpty());
    for (Title actual : titles) {
      Assert.assertNotNull(actual);
      Assert.assertTrue(UtilsRepository.assertEquals(expecteted1, actual)
          || UtilsRepository.assertEquals(expecteted2, actual));
    }

    Title expecteted3 = UtilsRepository.createTitle3();
    Title title = titles.getContent().get(0);
    title.setCopyright(expecteted3.getCopyright());
    title.setEditionNumber(expecteted3.getEditionNumber());
    title.setImageFile(expecteted3.getImageFile());
    title.setTitle(expecteted3.getTitle());
    title.setLanguage(expecteted3.getLanguage());
    title.setPrice(expecteted3.getPrice());
    titleRepository.save(title);

    Optional<Title> optional = titleRepository.findById(title.getId());
    Assert.assertTrue(optional.isPresent());
    title = optional.get();
    Assert.assertTrue(UtilsRepository.assertEquals(expecteted3, title));

    for (Title actual : titles) {
      titleRepository.delete(actual);
    }

    titles = titleRepository.findAll(PageRequest.of(0, 10));
    Assert.assertNotNull(titles);
    Assert.assertTrue(titles.isEmpty());
  }

  @Test
  public void testTitles() throws Exception {

  }

}
