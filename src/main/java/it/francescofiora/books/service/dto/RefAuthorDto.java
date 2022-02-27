package it.francescofiora.books.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Ref Author Dto.
 */
@Getter
@Setter
@ToString(callSuper = true, includeFieldNames = true)
public class RefAuthorDto extends RefDto implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "Unique Author identifier", example = "1", required = true)
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
