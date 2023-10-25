package it.francescofiora.books.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Title Dto for create new Title.
 */
@Getter
@Setter
@ToString(callSuper = true, includeFieldNames = true)
public class NewTitleDto extends BaseTitleDto implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(requiredMode = RequiredMode.REQUIRED)
  @JsonProperty("publisher")
  @Valid
  @NotNull
  private RefPublisherDto publisher = new RefPublisherDto();

  @Schema(requiredMode = RequiredMode.REQUIRED)
  @JsonProperty("authors")
  @NotEmpty
  @Valid
  private List<RefAuthorDto> authors = new ArrayList<>();

  @Override
  public int hashCode() {
    return Objects.hash(getAuthors(), getCopyright(), getEditionNumber(), getImageFile(),
        getLanguage(), getPrice(), getPublisher(), getName());
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    var other = (NewTitleDto) obj;
    return Objects.equals(getAuthors(), other.getAuthors())
        && Objects.equals(getCopyright(), other.getCopyright())
        && Objects.equals(getEditionNumber(), other.getEditionNumber())
        && Objects.equals(getImageFile(), other.getImageFile())
        && Objects.equals(getLanguage(), other.getLanguage())
        && Objects.equals(getPrice(), other.getPrice())
        && Objects.equals(getPublisher(), other.getPublisher())
        && Objects.equals(getName(), other.getName());
  }
}
