package it.francescofiora.books.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Ref Publisher Dto.
 */
@Getter
@Setter
@ToString(callSuper = true, includeFieldNames = true)
public class RefPublisherDto extends RefDto implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "Unique Publisher identifier", example = "1",
      requiredMode = RequiredMode.REQUIRED)
  @JsonProperty("id")
  @NotNull
  private Long id;

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    return super.equals(obj);
  }
}
