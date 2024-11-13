package sopt.makers.authentication.support.code.support.failure;

import static lombok.AccessLevel.PRIVATE;

import sopt.makers.authentication.support.code.base.FailureCode;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public enum TokenFailure implements FailureCode {
  ;
  private final HttpStatus status;
  private final String message;
}
