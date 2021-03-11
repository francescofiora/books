package it.francescofiora.books.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

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
    if (obj == null) { 
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    NewPublisherDto other = (NewPublisherDto) obj;
    if (publisherName == null) {
      if (other.publisherName != null) {
        return false;
      }
    } else if (!publisherName.equals(other.publisherName)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "NewPublisherDto {publisherName='" + getPublisherName() + "'}";
  }
}
