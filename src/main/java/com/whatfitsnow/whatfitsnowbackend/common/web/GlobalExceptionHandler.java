package com.whatfitsnow.whatfitsnowbackend.common.web;

import com.whatfitsnow.whatfitsnowbackend.common.api.ApiErrorResponse;
import com.whatfitsnow.whatfitsnowbackend.common.exception.ConflictException;
import com.whatfitsnow.whatfitsnowbackend.common.exception.NotFoundException;
import com.whatfitsnow.whatfitsnowbackend.common.exception.UnauthorizedException;
import com.whatfitsnow.whatfitsnowbackend.common.valueobject.InvalidValueException;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
    List<ApiErrorResponse.FieldViolation> violations = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .sorted(Comparator.comparing(FieldError::getField))
        .map(err -> new ApiErrorResponse.FieldViolation(err.getField(), err.getDefaultMessage()))
        .toList();

    return toResponse(HttpStatus.BAD_REQUEST, "Validation failed", request.getRequestURI(), violations);
  }

  @ExceptionHandler(ConflictException.class)
  public ResponseEntity<ApiErrorResponse> handleConflict(ConflictException ex, HttpServletRequest request) {
    return toResponse(HttpStatus.CONFLICT, ex.getMessage(), request.getRequestURI(), List.of());
  }

  @ExceptionHandler(UnauthorizedException.class)
  public ResponseEntity<ApiErrorResponse> handleUnauthorized(UnauthorizedException ex, HttpServletRequest request) {
    return toResponse(HttpStatus.UNAUTHORIZED, ex.getMessage(), request.getRequestURI(), List.of());
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ApiErrorResponse> handleNotFound(NotFoundException ex, HttpServletRequest request) {
    return toResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI(), List.of());
  }

  @ExceptionHandler(InvalidValueException.class)
  public ResponseEntity<ApiErrorResponse> handleInvalidValue(InvalidValueException ex, HttpServletRequest request) {
    List<ApiErrorResponse.FieldViolation> violations = List.of(
        new ApiErrorResponse.FieldViolation(ex.getField(), ex.getMessage())
    );
    return toResponse(HttpStatus.BAD_REQUEST, "Validation failed", request.getRequestURI(), violations);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiErrorResponse> handleGeneric(Exception ex, HttpServletRequest request) {
    return toResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error", request.getRequestURI(), List.of());
  }

  private static ResponseEntity<ApiErrorResponse> toResponse(
      HttpStatus status,
      String message,
      String path,
      List<ApiErrorResponse.FieldViolation> violations
  ) {
    ApiErrorResponse body = new ApiErrorResponse(
        Instant.now(),
        status.value(),
        status.getReasonPhrase(),
        message,
        path,
        violations
    );
    return ResponseEntity.status(status).body(body);
  }
}

