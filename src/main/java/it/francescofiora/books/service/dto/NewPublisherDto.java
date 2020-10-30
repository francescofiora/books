package it.francescofiora.books.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

@Validated
public class NewPublisherDto implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "Publisher Name", example = "Publisher Ltd", required = true)
  @JsonProperty("publisherName")
  private String publisherName;

  @NotBlank
  @Size(min = 2)
  public String getPublisherName() {
    return publisherName;
  }

  public void setPublisherName(String publisherName) {
    this.publisherName = publisherName;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((publisherName == null) ? 0 : publisherName.hashCode());
    return result;
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
