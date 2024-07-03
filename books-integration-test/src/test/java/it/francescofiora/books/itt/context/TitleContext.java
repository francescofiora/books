package it.francescofiora.books.itt.context;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

/**
 * Title Context.
 */
@Getter
@Setter
public class TitleContext {

  private JSONObject title;
  private Long titleId;
}
