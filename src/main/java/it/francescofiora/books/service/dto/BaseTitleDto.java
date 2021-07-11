package it.francescofiora.books.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import it.francescofiora.books.domain.enumeration.Language;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Abstract Title Dto.
 */
@Getter
@Setter
public abstract class BaseTitleDto {

  @Schema(description = "Book's Title", required = true)
  @JsonProperty("title")
  @NotBlank
  @Size(min = 3, max = 20)
  private String title;

  @Schema(description = "Edition Number", required = true)
  @JsonProperty("editionNumber")
  @NotNull
  private Long editionNumber;

  @Schema(description = "Language", required = true)
  @JsonProperty("language")
  @NotNull
  private Language language;

  @Schema(description = "Year of copyright", example = "2020", required = true)
  @JsonProperty("copyright")
  @NotNull
  private Integer copyright;

  @Schema(description = "Image File", example = "image.jpg")
  @JsonProperty("imageFile")
  private String imageFile;

  @Schema(description = "Price", example = "10", required = true)
  @JsonProperty("price")
  @NotNull
  @Positive
  private Long price;
}
