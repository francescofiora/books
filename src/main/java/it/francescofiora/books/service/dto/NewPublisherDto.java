package it.francescofiora.books.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Publisher Dto for create new Publisher.
 */
@Getter
@Setter
public class NewPublisherDto implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "Publisher Name", example = "Publisher Ltd", required = true)
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

  @Override
  public String toString() {
    return "NewPublisherDto {publisherName='" + getPublisherName() + "'}";
  }
}
