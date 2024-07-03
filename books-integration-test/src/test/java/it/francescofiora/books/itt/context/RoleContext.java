package it.francescofiora.books.itt.context;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

/**
 * Role Context.
 */
@Getter
@Setter
public class RoleContext {

  private JSONObject role;
  private Long roleId;

}
