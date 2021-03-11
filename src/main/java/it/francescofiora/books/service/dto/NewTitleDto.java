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
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    NewTitleDto other = (NewTitleDto) obj;
    if (authors == null) {
      if (other.authors != null) {
        return false;
      }
    } else if (!authors.equals(other.authors)) {
      return false;
    }
    if (getCopyright() == null) {
      if (other.getCopyright() != null) {
        return false;
      }
    } else if (!getCopyright().equals(other.getCopyright())) {
      return false;
    }
    if (getEditionNumber() == null) {
      if (other.getEditionNumber() != null) {
        return false;
      }
    } else if (!getEditionNumber().equals(other.getEditionNumber())) {
      return false;
    }
    if (getImageFile() == null) {
      if (other.getImageFile() != null) {
        return false;
      }
    } else if (!getImageFile().equals(other.getImageFile())) {
      return false;
    }
    if (getLanguage() != other.getLanguage()) {
      return false;
    }
    if (getPrice() == null) {
      if (other.getPrice() != null) {
        return false;
      }
    } else if (!getPrice().equals(other.getPrice())) {
      return false;
    }
    if (getPublisher() == null) {
      if (other.getPublisher() != null) {
        return false;
      }
    } else if (!getPublisher().equals(other.getPublisher())) {
      return false;
    }
    if (getTitle() == null) {
      if (other.getTitle() != null) {
        return false;
      }
    } else if (!getTitle().equals(other.getTitle())) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "TitleDto{title='" + getTitle() + "'" + ", editionNumber=" + getEditionNumber()
        + ", language='" + getLanguage() + "'" + ", copyright=" + getCopyright() + ", imageFile='"
        + getImageFile() + "'" + ", price=" + getPrice() + ", publisher=" + getPublisher()
        + ", authors='" + getAuthors() + "'" + "}";
  }
}
