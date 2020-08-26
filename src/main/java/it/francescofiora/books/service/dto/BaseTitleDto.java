package it.francescofiora.books.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

import it.francescofiora.books.domain.enumeration.Language;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public abstract class BaseTitleDto {

  @Schema(description = "Book's Title", required = true)
  @JsonProperty("title")
  private String title;

  @Schema(description = "Edition Number", required = true)
  @JsonProperty("editionNumber")
  private Long editionNumber;

  @Schema(description = "Language", required = true)
  @JsonProperty("language")
  private Language language;

  @Schema(description = "Year of copyright", example = "2020", required = true)
  @JsonProperty("copyright")
  private Integer copyright;

  @Schema(description = "Image File", example = "image.jpg")
  @JsonProperty("imageFile")
  private String imageFile;

  @Schema(description = "Price", example = "10", required = true)
  @JsonProperty("price")
  private Long price;

  @NotBlank
  @Size(min = 3, max = 20)
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @NotNull
  public Long getEditionNumber() {
    return editionNumber;
  }

  public void setEditionNumber(Long editionNumber) {
    this.editionNumber = editionNumber;
  }

  @NotNull
  public Language getLanguage() {
    return language;
  }

  public void setLanguage(Language language) {
    this.language = language;
  }

  @NotNull
  public Integer getCopyright() {
    return copyright;
  }

  public void setCopyright(Integer copyright) {
    this.copyright = copyright;
  }

  public String getImageFile() {
    return imageFile;
  }

  public void setImageFile(String imageFile) {
    this.imageFile = imageFile;
  }

  @NotNull
  public Long getPrice() {
    return price;
  }

  public void setPrice(Long price) {
    this.price = price;
  }

}
