package it.francescofiora.books.service.dto;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import it.francescofiora.books.domain.enumeration.Language;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Abstract Title Dto.
 */
@Getter
@Setter
public abstract class BaseTitleDto {

  @Schema(description = "Book's Name", example = "My prefer Book", requiredMode = REQUIRED)
  @JsonProperty("name")
  @NotBlank
  @Size(min = 3, max = 20)
  private String name;

  @Schema(description = "Edition Number", requiredMode = REQUIRED)
  @JsonProperty("editionNumber")
  @NotNull
  private Long editionNumber;

  @Schema(description = "Language", requiredMode = REQUIRED)
  @JsonProperty("language")
  @NotNull
  private Language language;

  @Schema(description = "Year of copyright", example = "2020", requiredMode = REQUIRED)
  @JsonProperty("copyright")
  @NotNull
  private Integer copyright;

  @Schema(description = "Image File", example = "image.jpg")
  @JsonProperty("imageFile")
  private String imageFile;

  @Schema(description = "Price", example = "10", requiredMode = REQUIRED)
  @JsonProperty("price")
  @NotNull
  @Positive
  private Long price;
}
