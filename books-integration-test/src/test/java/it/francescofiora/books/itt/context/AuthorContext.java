package it.francescofiora.books.itt.context;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;

/**
 * Author Context.
 */
@Getter
@Setter
public class AuthorContext {

  private JSONObject newAuthor;
  private JSONObject author;
  private Long authorId;
  private ResponseEntity<String> resultAuthors;
}
