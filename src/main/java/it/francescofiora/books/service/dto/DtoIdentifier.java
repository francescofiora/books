package it.francescofiora.books.service.dto;

import jakarta.validation.constraints.NotNull;

/**
 * Dto Identifier.
 */
public interface DtoIdentifier {
  Long getId();

  void setId(@NotNull Long id);
}
