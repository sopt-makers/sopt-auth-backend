package sopt.makers.authentication.support.exception;

import static sopt.makers.authentication.support.code.support.failure.CommonFailure.INTERNAL_SERVER_ERROR;
import static sopt.makers.authentication.support.code.support.failure.CommonFailure.INVALID_INPUT_VALUE;
import static sopt.makers.authentication.support.code.support.failure.CommonFailure.INVALID_REQUEST_BODY;
import static sopt.makers.authentication.support.code.support.failure.CommonFailure.METHOD_ARGUMENT_TYPE_MISMATCH;
import static sopt.makers.authentication.support.code.support.failure.CommonFailure.METHOD_NOT_SUPPORTED;
import static sopt.makers.authentication.support.code.support.failure.CommonFailure.MISSING_REQUEST_HEADER;
import static sopt.makers.authentication.support.code.support.failure.CommonFailure.NOT_FOUND_URL;
import static sopt.makers.authentication.support.code.support.failure.CommonFailure.NO_RESOURCE_FOUND;

import sopt.makers.authentication.support.common.api.BaseResponse;
import sopt.makers.authentication.support.exception.base.BaseException;
import sopt.makers.authentication.support.util.ResponseUtil;

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

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ApplicationExceptionHandler {
  @ExceptionHandler(Exception.class)
  ResponseEntity<BaseResponse<?>> handleInternalException(final Exception e) {
    log.error(e.getMessage());
    return ResponseUtil.failure(INTERNAL_SERVER_ERROR, e.getMessage());
  }

  @ExceptionHandler(BaseException.class)
  ResponseEntity<BaseResponse<?>> handleBusinessException(final BaseException e) {
    log.warn(e.getError().getMessage());
    return ResponseUtil.failure(e.getError());
  }

  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<BaseResponse<?>> handleNoResourceFoundException(
      final NoResourceFoundException e) {
    log.warn(e.getMessage());
    return ResponseUtil.failure(NO_RESOURCE_FOUND);
  }

  @ExceptionHandler(NoHandlerFoundException.class)
  public ResponseEntity<BaseResponse<?>> handleNoHandlerFoundException(
      final NoHandlerFoundException e) {
    log.warn(e.getMessage());
    return ResponseUtil.failure(NOT_FOUND_URL);
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<BaseResponse<?>> handleHttpRequestMethodNotSupportedException(
      final HttpRequestMethodNotSupportedException e) {
    log.warn(e.getMessage());
    return ResponseUtil.failure(METHOD_NOT_SUPPORTED);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<BaseResponse<?>> handleTypeMismatch(
      final MethodArgumentTypeMismatchException e) {
    log.warn(e.getMessage());
    return ResponseUtil.failure(METHOD_ARGUMENT_TYPE_MISMATCH);
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
    return ResponseUtil.failure(INVALID_INPUT_VALUE, errorDetails);
  }

  @ExceptionHandler(MissingRequestHeaderException.class)
  public ResponseEntity<BaseResponse<?>> handleMissingHeaderException(
      final MissingRequestHeaderException e) {
    log.warn(e.getMessage());
    return ResponseUtil.failure(MISSING_REQUEST_HEADER);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<BaseResponse<?>> handleNotReadableException(
      final HttpMessageNotReadableException e) {
    log.warn(e.getMessage());
    return ResponseUtil.failure(INVALID_REQUEST_BODY);
  }
}
