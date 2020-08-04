package it.francescofiora.books.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

@Validated
public class UpdatebleTitleDto extends BaseTitleDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Schema(required = true)
  @JsonProperty("publisher")
  @Valid
  private RefPublisherDto publisher = new RefPublisherDto();

  @Schema(required = true)
  @JsonProperty("authors")
  @Valid
  private List<RefAuthorDto> authors = new ArrayList<>();

  @NotNull
  public RefPublisherDto getPublisher() {
    return publisher;
  }

  public void setPublisher(RefPublisherDto publisher) {
    this.publisher = publisher;
  }

  @NotNull
  @Valid
  public List<RefAuthorDto> getAuthors() {
    return authors;
  }

  public void setAuthors(List<RefAuthorDto> authors) {
    this.authors = authors;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    TitleDto titleDto = (TitleDto) o;
    if (titleDto.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), titleDto.getId());
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