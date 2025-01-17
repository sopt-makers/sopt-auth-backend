package sopt.makers.authentication.support.exception;

import static sopt.makers.authentication.support.code.support.failure.CommonFailure.INTERNAL_SERVER_ERROR;

import sopt.makers.authentication.support.common.api.BaseResponse;
import sopt.makers.authentication.support.exception.domain.AuthException;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ApplicationExceptionHandler {
  @ExceptionHandler(RuntimeException.class)
  ResponseEntity<BaseResponse<?>> handleInternalException(final RuntimeException e) {
    log.error(e.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(BaseResponse.ofFailure(INTERNAL_SERVER_ERROR));
  }

  @ExceptionHandler(AuthException.class)
  ResponseEntity<BaseResponse<?>> handleAuthException(final AuthException e) {
    log.error(e.getError().getMessage());
    return ResponseEntity.status(e.getError().getStatus().value())
        .body(BaseResponse.ofFailure(e.getError()));
  }
}
