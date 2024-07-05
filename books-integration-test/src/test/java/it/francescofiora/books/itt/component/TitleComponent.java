package it.francescofiora.books.itt.component;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.books.itt.context.AuthorContext;
import it.francescofiora.books.itt.context.PublisherContext;
import it.francescofiora.books.itt.context.TitleContext;
import it.francescofiora.books.itt.util.JsonException;
import it.francescofiora.books.itt.util.UtilTests;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;

/**
 * Title Component.
 */
@RequiredArgsConstructor
public class TitleComponent {

  private final BookApiService bookApiService;
  private final AuthorContext authorContext;
  private final PublisherContext publisherContext;
  private final TitleContext titleContext;

  /**
   * Create Title.
   */
  public void createTitle() {
    var title = UtilTests.createTitle();
    try {
      title.put("publisher", UtilTests.createRef(publisherContext.getPublisherId()));
      title.put("authors", UtilTests.createRefs(List.of(authorContext.getAuthorId())));
    } catch (JSONException e) {
      throw new JsonException(e.getMessage());
    }
    titleContext.setNewTitle(title);
    var titleId = bookApiService.createTitle(title.toString());
    titleContext.setTitleId(titleId);
  }

  /**
   * Update Title.
   */
  public void updateTitle() {
    UtilTests.updateTitle(titleContext.getNewTitle());
    var resultVoid = bookApiService.updateTitle(
        titleContext.getTitleId(), titleContext.getNewTitle().toString());
    assertThat(resultVoid.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  /**
   * Get Title By Id.
   */
  public void getTitleById() {
    var resultTitle = bookApiService.getTitleById(titleContext.getTitleId());
    assertThat(resultTitle.getStatusCode()).isEqualTo(HttpStatus.OK);
    try {
      titleContext.setTitle(new JSONObject(resultTitle.getBody()));
    } catch (JSONException e) {
      throw new JsonException(e.getMessage());
    }
  }

  /**
   * Find Titles.
   */
  public void findTitles() {
    var resultTitles = bookApiService.findTitles();
    assertThat(resultTitles.getStatusCode()).isEqualTo(HttpStatus.OK);
    titleContext.setResultTitles(resultTitles);
  }

  public void deleteTitleById() {
    var result = bookApiService.deleteTitleById(titleContext.getTitleId());
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
  }

  /**
   * Check Title.
   */
  public void checkTitle() {
    var title = titleContext.getTitle();
    var newTitle = titleContext.getNewTitle();
    try {
      assertThat(title.getString(UtilTests.NAME)).isEqualTo(newTitle.getString(UtilTests.NAME));
      assertThat(title.getLong(UtilTests.EDITION_NUMBER))
          .isEqualTo(newTitle.getLong(UtilTests.EDITION_NUMBER));
      assertThat(title.getString(UtilTests.LANGUAGE))
          .isEqualTo(newTitle.getString(UtilTests.LANGUAGE));
      assertThat(title.getString(UtilTests.COPYRIGHT))
          .isEqualTo(newTitle.getString(UtilTests.COPYRIGHT));
      assertThat(title.getString(UtilTests.IMAGE_FILE))
          .isEqualTo(newTitle.getString(UtilTests.IMAGE_FILE));
      assertThat(title.getLong(UtilTests.PRICE)).isEqualTo(newTitle.getLong(UtilTests.PRICE));
    } catch (JSONException e) {
      throw new JsonException(e.getMessage());
    }
    titleContext.setNewTitle(title);
  }
}
