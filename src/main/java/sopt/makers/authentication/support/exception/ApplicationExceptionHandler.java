package sopt.makers.authentication.support.exception;

import sopt.makers.authentication.support.common.api.BaseResponse;
import sopt.makers.authentication.support.exception.domain.AuthException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ApplicationExceptionHandler {

  @ExceptionHandler(AuthException.class)
  ResponseEntity<BaseResponse<?>> authFailureException(final AuthException e) {
    log.error(e.getError().getMessage());
    return ResponseEntity.status(e.getError().getStatus().value())
        .body(BaseResponse.ofFailure(e.getError()));
  }
}
