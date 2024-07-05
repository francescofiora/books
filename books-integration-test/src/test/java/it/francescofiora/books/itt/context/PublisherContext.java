package it.francescofiora.books.itt.context;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;

/**
 * Publisher Context.
 */
@Getter
@Setter
public class PublisherContext {

  private JSONObject newPublisher;
  private JSONObject publisher;
  private Long publisherId;
  private ResponseEntity<String> resultPublishers;
}
