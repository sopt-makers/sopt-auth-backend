package sopt.makers.authentication.adapter.in.web.common.exception;

import static sopt.makers.authentication.adapter.in.web.common.exception.CommonFailure.INTERNAL_SERVER_ERROR;
import static sopt.makers.authentication.adapter.in.web.common.exception.CommonFailure.INVALID_INPUT_VALUE;
import static sopt.makers.authentication.adapter.in.web.common.exception.CommonFailure.INVALID_REQUEST_BODY;
import static sopt.makers.authentication.adapter.in.web.common.exception.CommonFailure.METHOD_ARGUMENT_TYPE_MISMATCH;
import static sopt.makers.authentication.adapter.in.web.common.exception.CommonFailure.METHOD_NOT_SUPPORTED;
import static sopt.makers.authentication.adapter.in.web.common.exception.CommonFailure.MISSING_REQUEST_HEADER;
import static sopt.makers.authentication.adapter.in.web.common.exception.CommonFailure.NOT_FOUND_URL;
import static sopt.makers.authentication.adapter.in.web.common.exception.CommonFailure.NO_RESOURCE_FOUND;

import sopt.makers.authentication.adapter.in.web.common.BaseResponse;
import sopt.makers.authentication.adapter.in.web.util.ResponseUtil;
import sopt.makers.authentication.common.exception.BaseException;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
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

import io.sentry.Sentry;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ApplicationExceptionHandler {

  private static final String VALIDATION_KEY_FORMAT = "valid_%s";

  @ExceptionHandler(Exception.class)
  ResponseEntity<BaseResponse<?>> handleInternalException(final Exception e) {
    Sentry.captureException(e);
    log.error(e.getMessage());
    return ResponseUtil.failure(INTERNAL_SERVER_ERROR, e.getMessage());
  }

  @ExceptionHandler(BaseException.class)
  ResponseEntity<BaseResponse<?>> handleBusinessException(final BaseException e) {
    Sentry.captureException(e);
    log.warn(e.getError().getMessage());
    return ResponseUtil.failure(e.getError());
  }

  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<BaseResponse<?>> handleNoResourceFoundException(
      final NoResourceFoundException e) {
    Sentry.captureException(e);
    log.warn(e.getMessage());
    return ResponseUtil.failure(NO_RESOURCE_FOUND);
  }

  @ExceptionHandler(NoHandlerFoundException.class)
  public ResponseEntity<BaseResponse<?>> handleNoHandlerFoundException(
      final NoHandlerFoundException e) {
    Sentry.captureException(e);
    log.warn(e.getMessage());
    return ResponseUtil.failure(NOT_FOUND_URL);
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<BaseResponse<?>> handleHttpRequestMethodNotSupportedException(
      final HttpRequestMethodNotSupportedException e) {
    Sentry.captureException(e);
    log.warn(e.getMessage());
    return ResponseUtil.failure(METHOD_NOT_SUPPORTED);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<BaseResponse<?>> handleTypeMismatch(
      final MethodArgumentTypeMismatchException e) {
    Sentry.captureException(e);
    log.warn(e.getMessage());
    return ResponseUtil.failure(METHOD_ARGUMENT_TYPE_MISMATCH);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<BaseResponse<?>> handleMethodArgumentNotValidException(
      final MethodArgumentNotValidException e) {
    Sentry.captureException(e);
    log.warn(e.getMessage());
    Errors errors = e.getBindingResult();
    Map<String, String> errorDetails = new HashMap<>();

    for (FieldError error : errors.getFieldErrors()) {
      String validKeyName = String.format(VALIDATION_KEY_FORMAT, error.getField());
      errorDetails.put(validKeyName, error.getDefaultMessage());
    }
    return ResponseUtil.failure(INVALID_INPUT_VALUE, errorDetails);
  }

  @ExceptionHandler(MissingRequestHeaderException.class)
  public ResponseEntity<BaseResponse<?>> handleMissingHeaderException(
      final MissingRequestHeaderException e) {
    Sentry.captureException(e);
    log.warn(e.getMessage());
    return ResponseUtil.failure(MISSING_REQUEST_HEADER);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<BaseResponse<?>> handleNotReadableException(
      final HttpMessageNotReadableException e) {
    Sentry.captureException(e);
    log.warn(e.getMessage());
    return ResponseUtil.failure(INVALID_REQUEST_BODY);
  }
}
