package it.francescofiora.books.itt.context;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

/**
 * Publisher Context.
 */
@Getter
@Setter
public class PublisherContext {

  private JSONObject publisher;
  private Long publisherId;
}
