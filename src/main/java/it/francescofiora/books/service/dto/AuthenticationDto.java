package it.francescofiora.books.service.dto;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Authentication Dto.
 */
@Getter
@Setter
@ToString(callSuper = true, includeFieldNames = true)
public class AuthenticationDto implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @Schema(description = "Token", requiredMode = REQUIRED)
  @JsonProperty("token")
  @NotBlank
  private String token;


  @Override
  public int hashCode() {
    return Objects.hash(getToken());
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    var other = (AuthenticationDto) obj;
    return Objects.equals(getToken(), other.getToken());
  }
}
