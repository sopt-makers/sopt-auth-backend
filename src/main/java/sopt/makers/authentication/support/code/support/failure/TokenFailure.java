package sopt.makers.authentication.support.code.support.failure;

import sopt.makers.authentication.support.code.base.FailureCode;

import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum TokenFailure implements FailureCode {
  ;
  private final HttpStatus status;
  private final String message;
}
