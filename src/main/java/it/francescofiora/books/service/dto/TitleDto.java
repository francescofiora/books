package it.francescofiora.books.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import it.francescofiora.books.service.util.DtoUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Title Dto.
 */
@Getter
@Setter
@ToString(callSuper = true, includeFieldNames = true)
public class TitleDto extends BaseTitleDto implements Serializable, DtoIdentifier {

  private static final long serialVersionUID = 1L;

  @Schema(description = "Unique Title identifier", example = "1",
      requiredMode = RequiredMode.REQUIRED)
  @JsonProperty("id")
  @NotNull
  private Long id;

  @Schema(requiredMode = RequiredMode.REQUIRED)
  @JsonProperty("publisher")
  @NotNull
  @Valid
  private PublisherDto publisher = new PublisherDto();

  @Schema(requiredMode = RequiredMode.REQUIRED)
  @JsonProperty("authors")
  @NotEmpty
  @Valid
  private List<AuthorDto> authors = new ArrayList<>();

  @Override
  public boolean equals(Object obj) {
    return DtoUtils.equals(this, obj);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }
}
