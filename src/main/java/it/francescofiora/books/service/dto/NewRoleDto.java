package it.francescofiora.books.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Role Dto for create new Role.
 */
@Getter
@Setter
@ToString(callSuper = true, includeFieldNames = true)
public class NewRoleDto implements Serializable {

  private static final long serialVersionUID = 1L;
  
  @Schema(description = "Role's Name", example = "book_read", required = true)
  @JsonProperty("name")
  @NotBlank
  @Size(max = 100)
  private String name;

  @Schema(description = "Description of the Role", example = "Book read permission",
      required = true)
  @JsonProperty("description")
  @NotBlank
  @Size(max = 255)
  private String description;
  
  @Schema(required = true)
  @JsonProperty("permissions")
  @NotEmpty
  @Valid
  private List<RefPermissionDto> permissions = new ArrayList<>();

  @Override
  public int hashCode() {
    return Objects.hash(getName(), getDescription(), getPermissions());
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    var other = (NewRoleDto) obj;
    return Objects.equals(getName(), other.getName())
        && Objects.equals(getDescription(), other.getDescription())
        && Objects.equals(getPermissions(), other.getPermissions());
  }
}
