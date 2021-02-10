package it.francescofiora.books.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatebleTitleDto extends BaseTitleDto implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "Unique Title identifier", example = "1", required = true)
  @JsonProperty("id")
  @NotNull
  private Long id;

  @Schema(required = true)
  @JsonProperty("publisher")
  @NotNull
  @Valid
  private RefPublisherDto publisher = new RefPublisherDto();

  @Schema(required = true)
  @JsonProperty("authors")
  @NotEmpty
  @Valid
  private List<RefAuthorDto> authors = new ArrayList<>();

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    UpdatebleTitleDto updatebleTitleDto = (UpdatebleTitleDto) o;
    if (updatebleTitleDto.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), updatebleTitleDto.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "UpdatebleTitleDto{" + "id=" + getId() + ", title='" + getTitle() + "'"
        + ", editionNumber=" + getEditionNumber() + ", language='" + getLanguage() + "'"
        + ", copyright=" + getCopyright() + ", imageFile='" + getImageFile() + "'" + ", price="
        + getPrice() + ", publisher=" + getPublisher() + ", authors='" + getAuthors() + "'" + "}";
  }
}
