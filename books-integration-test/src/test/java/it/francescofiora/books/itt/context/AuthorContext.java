package it.francescofiora.books.itt.context;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

/**
 * Author Context.
 */
@Getter
@Setter
public class AuthorContext {

  private JSONObject author;
  private Long authorId;
}
