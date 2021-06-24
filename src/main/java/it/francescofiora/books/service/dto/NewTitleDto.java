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
public class NewTitleDto extends BaseTitleDto implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(required = true)
  @JsonProperty("publisher")
  @Valid
  @NotNull
  private RefPublisherDto publisher = new RefPublisherDto();

  @Schema(required = true)
  @JsonProperty("authors")
  @NotEmpty
  @Valid
  private List<RefAuthorDto> authors = new ArrayList<>();

  @Override
  public int hashCode() {
    return Objects.hash(getAuthors(), getCopyright(), getEditionNumber(), getImageFile(),
        getLanguage(), getPrice(), getPublisher(), getTitle());
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
        && Objects.equals(getTitle(), other.getTitle());
  }

  @Override
  public String toString() {
    return "TitleDto{title='" + getTitle() + "'" + ", editionNumber=" + getEditionNumber()
        + ", language='" + getLanguage() + "'" + ", copyright=" + getCopyright() + ", imageFile='"
        + getImageFile() + "'" + ", price=" + getPrice() + ", publisher=" + getPublisher()
        + ", authors='" + getAuthors() + "'" + "}";
  }
}
