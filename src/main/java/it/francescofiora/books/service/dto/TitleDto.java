package it.francescofiora.books.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
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

/**
 * Title Dto.
 */
@Getter
@Setter
public class TitleDto extends BaseTitleDto implements Serializable, DtoIdentifier {

  private static final long serialVersionUID = 1L;

  @Schema(description = "Unique Title identifier", example = "1", required = true)
  @JsonProperty("id")
  @NotNull
  private Long id;

  @Schema(required = true)
  @JsonProperty("publisher")
  @NotNull
  @Valid
  private PublisherDto publisher = new PublisherDto();

  @Schema(required = true)
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

  @Override
  public String toString() {
    return "TitleDto{" + "id=" + getId() + ", title='" + getTitle() + "'" + ", editionNumber="
        + getEditionNumber() + ", language='" + getLanguage() + "'" + ", copyright="
        + getCopyright() + ", imageFile='" + getImageFile() + "'" + ", price=" + getPrice()
        + ", publisher=" + getPublisher() + ", authors='" + getAuthors() + "'" + "}";
  }
}
