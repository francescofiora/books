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
 * Signin Dto.
 */
@Getter
@Setter
@ToString(callSuper = true, includeFieldNames = true)
public class SigninDto implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "User Name", example = "John", requiredMode = REQUIRED)
  @JsonProperty("username")
  @NotBlank
  @Size(min = 2)
  private String username;

  @Schema(description = "Password", requiredMode = REQUIRED)
  @NotBlank
  @Size(min = 8)
  private String password;

  @Override
  public int hashCode() {
    return Objects.hash(getUsername(), getPassword());
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    var other = (SigninDto) obj;
    return Objects.equals(getUsername(), other.getUsername())
        && Objects.equals(getPassword(), other.getPassword());
  }
}
