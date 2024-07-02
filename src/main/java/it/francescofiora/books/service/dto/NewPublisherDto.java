package it.francescofiora.books.service.dto;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Publisher Dto for create new Publisher.
 */
@Getter
@Setter
@ToString(callSuper = true, includeFieldNames = true)
public class NewPublisherDto implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @Schema(description = "Publisher Name", example = "Publisher Ltd", requiredMode = REQUIRED)
  @JsonProperty("publisherName")
  @NotBlank
  @Size(min = 2)
  private String publisherName;

  @Override
  public int hashCode() {
    return Objects.hashCode(getPublisherName());
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    return Objects.equals(getPublisherName(), ((NewPublisherDto) obj).getPublisherName());
  }
}
