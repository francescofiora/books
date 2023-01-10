package it.francescofiora.books.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import it.francescofiora.books.service.util.DtoUtils;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Permission Dto.
 */
@Getter
@Setter
@ToString(callSuper = true, includeFieldNames = true)
public class PermissionDto implements DtoIdentifier, Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "Unique identifier", example = "1", requiredMode = RequiredMode.REQUIRED)
  @JsonProperty("id")
  @NotNull
  private Long id;

  @Schema(description = "Permission's Name", example = "book_read",
      requiredMode = RequiredMode.REQUIRED)
  @JsonProperty("name")
  @NotBlank
  @Size(max = 100)
  private String name;

  @Schema(description = "Description of the Permission", example = "Book read permission",
      requiredMode = RequiredMode.REQUIRED)
  @JsonProperty("description")
  @NotBlank
  private String description;

  @Override
  public boolean equals(Object obj) {
    return DtoUtils.equals(this, obj);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }
}
