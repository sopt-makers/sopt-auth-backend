package sopt.makers.authentication.support.code.domain.failure;

import static lombok.AccessLevel.PRIVATE;

import sopt.makers.authentication.support.code.base.FailureCode;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public enum AuthFailure implements FailureCode {
  INVALID_SOCIAL_PLATFORM(HttpStatus.BAD_REQUEST, "지원하지 않는 소셜 플랫폼입니다"),
  ;
  private final HttpStatus status;
  private final String message;
}
