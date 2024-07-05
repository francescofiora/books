package it.francescofiora.books.itt.context;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;

/**
 * Title Context.
 */
@Getter
@Setter
public class TitleContext {

  private JSONObject newTitle;
  private JSONObject title;
  private Long titleId;
  private ResponseEntity<String> resultTitles;
}
