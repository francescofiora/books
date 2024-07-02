package it.francescofiora.books.service.dto;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import it.francescofiora.books.service.util.DtoUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Title Dto for update Title.
 */
@Getter
@Setter
@ToString(callSuper = true, includeFieldNames = true)
public class UpdatebleTitleDto extends BaseTitleDto implements Serializable, DtoIdentifier {

  @Serial
  private static final long serialVersionUID = 1L;

  @Schema(description = "Unique Title identifier", example = "1", requiredMode = REQUIRED)
  @JsonProperty("id")
  @NotNull
  private Long id;

  @Schema(requiredMode = REQUIRED)
  @JsonProperty("publisher")
  @NotNull
  @Valid
  private RefPublisherDto publisher = new RefPublisherDto();

  @Schema(requiredMode = REQUIRED)
  @JsonProperty("authors")
  @NotEmpty
  @Valid
  private List<RefAuthorDto> authors = new ArrayList<>();

  @Override
  public boolean equals(Object obj) {
    return DtoUtils.equals(this, obj);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }
}
