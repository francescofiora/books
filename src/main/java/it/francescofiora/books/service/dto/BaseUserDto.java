package it.francescofiora.books.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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

  private static final long serialVersionUID = 1L;

  @Schema(description = "User Name", example = "John", requiredMode = RequiredMode.REQUIRED)
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

  @Schema(requiredMode = RequiredMode.REQUIRED)
  @JsonProperty("roles")
  @NotEmpty
  @Valid
  private List<RefRoleDto> roles = new ArrayList<>();
}
