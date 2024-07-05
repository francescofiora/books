package it.francescofiora.books.itt.context;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;

/**
 * Role Context.
 */
@Getter
@Setter
public class RoleContext {

  private JSONObject newRole;
  private JSONObject role;
  private Long roleId;
  private ResponseEntity<String> resultRoles;
  private ResponseEntity<String> resultPermissions;
}
