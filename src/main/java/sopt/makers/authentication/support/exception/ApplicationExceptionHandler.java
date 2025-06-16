package sopt.makers.authentication.support.exception;

import static sopt.makers.authentication.support.code.support.failure.CommonFailure.*;

import sopt.makers.authentication.support.common.api.BaseResponse;
import sopt.makers.authentication.support.exception.base.BaseException;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ApplicationExceptionHandler {
  @ExceptionHandler(Exception.class)
  ResponseEntity<BaseResponse<?>> handleInternalException(final Exception e) {
    log.error(e.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(BaseResponse.ofFailure(INTERNAL_SERVER_ERROR));
  }

  @ExceptionHandler(BaseException.class)
  ResponseEntity<BaseResponse<?>> handleBusinessException(final BaseException e) {
    log.error(e.getError().getMessage());
    log.warn(e.getError().getMessage());
    return ResponseEntity.status(e.getError().getStatus().value())
        .body(BaseResponse.ofFailure(e.getError()));
  }

  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<BaseResponse<?>> handleNoResourceFoundException(
      final NoResourceFoundException e) {
    log.warn(e.getMessage());
    return ResponseEntity.status(NO_RESOURCE_FOUND.getStatus())
        .body(BaseResponse.ofFailure(NO_RESOURCE_FOUND));
  }

  @ExceptionHandler(NoHandlerFoundException.class)
  public ResponseEntity<BaseResponse<?>> handleNoHandlerFoundException(
      final NoHandlerFoundException e) {
    log.warn(e.getMessage());
    return ResponseEntity.status(NOT_FOUND_URL.getStatus())
        .body(BaseResponse.ofFailure(NOT_FOUND_URL));
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<BaseResponse<?>> handleHttpRequestMethodNotSupportedException(
      final HttpRequestMethodNotSupportedException e) {
    log.warn(e.getMessage());
    return ResponseEntity.status(METHOD_NOT_SUPPORTED.getStatus())
        .body(BaseResponse.ofFailure(METHOD_NOT_SUPPORTED));
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<BaseResponse<?>> handleTypeMismatch(
      final MethodArgumentTypeMismatchException e) {
    log.warn(e.getMessage());
    return ResponseEntity.status(METHOD_ARGUMENT_TYPE_MISMATCH.getStatus())
        .body(BaseResponse.ofFailure(METHOD_NOT_SUPPORTED));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<BaseResponse<?>> handleMethodArgumentNotValidException(
      final MethodArgumentNotValidException e) {
    log.warn(e.getMessage());
    Errors errors = e.getBindingResult();
    Map<String, String> errorDetails = new HashMap<>();

    for (FieldError error : errors.getFieldErrors()) {
      String validKeyName = String.format("valid_%s", error.getField());
      errorDetails.put(validKeyName, error.getDefaultMessage());
    }
    return ResponseEntity.status(INVALID_INPUT_VALUE.getStatus())
        .body(BaseResponse.ofFailure(INVALID_INPUT_VALUE, errorDetails));
  }

  @ExceptionHandler(MissingRequestHeaderException.class)
  public ResponseEntity<BaseResponse<?>> missingHeaderException(
      final MissingRequestHeaderException e) {
    log.warn(e.getMessage());
    return ResponseEntity.status(MISSING_REQUEST_HEADER.getStatus())
        .body(BaseResponse.ofFailure(MISSING_REQUEST_HEADER));
  }
}
