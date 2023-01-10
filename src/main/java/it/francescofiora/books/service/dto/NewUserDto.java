package it.francescofiora.books.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import java.util.Objects;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * User Dto for create new User.
 */
@Getter
@Setter
@ToString(callSuper = true, includeFieldNames = true)
public class NewUserDto extends BaseUserDto {

  private static final long serialVersionUID = 1L;

  @Schema(description = "Password", requiredMode = RequiredMode.REQUIRED)
  @NotBlank
  @Size(min = 8)
  private String password;

  @Override
  public int hashCode() {
    return Objects.hash(getUsername(), getPassword(), getEnabled(), getAccountNonExpired(),
        getAccountNonLocked(), getCredentialsNonExpired(), getRoles());
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    var other = (NewUserDto) obj;
    return Objects.equals(getUsername(), other.getUsername())
        && Objects.equals(getPassword(), other.getPassword())
        && Objects.equals(getEnabled(), other.getEnabled())
        && Objects.equals(getAccountNonExpired(), other.getAccountNonExpired())
        && Objects.equals(getAccountNonLocked(), other.getAccountNonLocked())
        && Objects.equals(getCredentialsNonExpired(), other.getCredentialsNonExpired())
        && Objects.equals(getRoles(), other.getRoles());
  }
}
