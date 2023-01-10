package it.francescofiora.books.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
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

  @Schema(description = "Book's Name", example = "My prefer Book",
      requiredMode = RequiredMode.REQUIRED)
  @JsonProperty("name")
  @NotBlank
  @Size(min = 3, max = 20)
  private String name;

  @Schema(description = "Edition Number", requiredMode = RequiredMode.REQUIRED)
  @JsonProperty("editionNumber")
  @NotNull
  private Long editionNumber;

  @Schema(description = "Language", requiredMode = RequiredMode.REQUIRED)
  @JsonProperty("language")
  @NotNull
  private Language language;

  @Schema(description = "Year of copyright", example = "2020", requiredMode = RequiredMode.REQUIRED)
  @JsonProperty("copyright")
  @NotNull
  private Integer copyright;

  @Schema(description = "Image File", example = "image.jpg")
  @JsonProperty("imageFile")
  private String imageFile;

  @Schema(description = "Price", example = "10", requiredMode = RequiredMode.REQUIRED)
  @JsonProperty("price")
  @NotNull
  @Positive
  private Long price;
}
