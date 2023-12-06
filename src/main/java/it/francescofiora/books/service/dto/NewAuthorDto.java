package it.francescofiora.books.service.dto;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Author Dto for create new Author.
 */
@Getter
@Setter
@ToString(callSuper = true, includeFieldNames = true)
public class NewAuthorDto implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "First Name", example = "John", requiredMode = REQUIRED)
  @JsonProperty("firstName")
  @NotBlank
  @Size(min = 2)
  private String firstName;

  @Schema(description = "Last Name", example = "Smith", requiredMode = REQUIRED)
  @JsonProperty("lastName")
  @NotBlank
  @Size(min = 2)
  private String lastName;

  @Override
  public int hashCode() {
    return Objects.hash(getFirstName(), getLastName());
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    var other = (NewAuthorDto) obj;
    return Objects.equals(getFirstName(), other.getFirstName())
        && Objects.equals(getLastName(), other.getLastName());
  }
}
