package it.francescofiora.books.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewAuthorDto implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "First Name", example = "John", required = true)
  @JsonProperty("firstName")
  @NotBlank
  @Size(min = 2)
  private String firstName;

  @Schema(description = "Last Name", example = "Smith", required = true)
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
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    NewAuthorDto other = (NewAuthorDto) obj;
    if (firstName == null) {
      if (other.firstName != null) {
        return false;
      }
    } else if (!firstName.equals(other.firstName)) {
      return false;
    }
    if (lastName == null) {
      if (other.lastName != null) {
        return false;
      }
    } else if (!lastName.equals(other.lastName)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "NewAuthorDto {firstName='" + getFirstName() + "', lastName='" + getLastName() + "'}";
  }
}
