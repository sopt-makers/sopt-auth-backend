package sopt.makers.authentication.adapter.in.web.common.exception;

import static sopt.makers.authentication.adapter.in.web.common.exception.CommonFailure.INTERNAL_SERVER_ERROR;

import sopt.makers.authentication.adapter.in.web.common.BaseResponse;
import sopt.makers.authentication.common.exception.BaseException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

  @ExceptionHandler(BaseException.class)
  ResponseEntity<BaseResponse<?>> handleBusinessException(final BaseException e) {
    log.error(e.getError().getMessage());
    return ResponseEntity.status(e.getError().getStatus().value())
        .body(BaseResponse.ofFailure(e.getError()));
  }
}
