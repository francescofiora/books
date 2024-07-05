package it.francescofiora.books.itt.component;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.books.itt.context.PublisherContext;
import it.francescofiora.books.itt.util.JsonException;
import it.francescofiora.books.itt.util.UtilTests;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;

/**
 * Publisher Component.
 */
@RequiredArgsConstructor
public class PublisherComponent {

  private final BookApiService bookApiService;
  private final PublisherContext publisherContext;

  /**
   * Create Publisher.
   */
  public void createPublisher() {
    var publisher = UtilTests.createPublisher();
    publisherContext.setNewPublisher(publisher);
    var publisherId = bookApiService.createPublisher(publisher.toString());
    publisherContext.setPublisherId(publisherId);
  }

  /**
   * Update Publisher.
   */
  public void updatePublisher() {
    UtilTests.updatePublisher(publisherContext.getNewPublisher());
    var resultVoid = bookApiService.updatePublisher(publisherContext.getPublisherId(),
        publisherContext.getNewPublisher().toString());
    assertThat(resultVoid.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  /**
   * Find Publishers.
   */
  public void findPublishers() {
    var resultPublishers = bookApiService.findPublishers();
    assertThat(resultPublishers.getStatusCode()).isEqualTo(HttpStatus.OK);
    publisherContext.setResultPublishers(resultPublishers);
  }

  /**
   * Get Publisher By Id.
   */
  public void getPublisherById() {
    var resultPublisher = bookApiService.getPublisherById(publisherContext.getPublisherId());
    assertThat(resultPublisher.getStatusCode()).isEqualTo(HttpStatus.OK);
    try {
      publisherContext.setPublisher(new JSONObject(resultPublisher.getBody()));
    } catch (JSONException e) {
      throw new JsonException(e.getMessage());
    }
  }

  public void deletePublisherById() {
    var result = bookApiService.deletePublisherById(publisherContext.getPublisherId());
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
  }

  /**
   * Check Publisher.
   */
  public void checkPublisher() {
    var publisher = publisherContext.getPublisher();
    var newPublisher = publisherContext.getNewPublisher();
    try {
      assertThat(publisher.getString(UtilTests.PUBLISHER_NAME))
          .isEqualTo(newPublisher.getString(UtilTests.PUBLISHER_NAME));
    } catch (JSONException e) {
      throw new JsonException(e.getMessage());
    }
    publisherContext.setNewPublisher(publisher);
  }
}
