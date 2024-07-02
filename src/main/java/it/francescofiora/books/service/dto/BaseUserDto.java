package it.francescofiora.books.service.dto;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Base User Dto.
 */
@Getter
@Setter
@ToString(callSuper = true, includeFieldNames = true)
public abstract class BaseUserDto implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @Schema(description = "User Name", example = "John", requiredMode = REQUIRED)
  @JsonProperty("username")
  @NotBlank
  @Size(min = 2)
  private String username;

  @Schema(description = "Enabled", example = "true")
  @NotNull
  private Boolean enabled;

  @Schema(description = "Account Non Expired", example = "true")
  @NotNull
  private Boolean accountNonExpired;

  @Schema(description = "Account Non Locked", example = "true")
  @NotNull
  private Boolean accountNonLocked;

  @Schema(description = "Credentials Non Expired", example = "true")
  @NotNull
  private Boolean credentialsNonExpired;

  @Schema(requiredMode = REQUIRED)
  @JsonProperty("roles")
  @NotEmpty
  @Valid
  private List<RefRoleDto> roles = new ArrayList<>();
}
