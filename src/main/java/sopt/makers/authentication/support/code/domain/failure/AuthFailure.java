package sopt.makers.authentication.support.code.domain.failure;

import static lombok.AccessLevel.PRIVATE;

import sopt.makers.authentication.support.code.base.FailureCode;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public enum AuthFailure implements FailureCode {
  // 400
  INVALID_SOCIAL_PLATFORM(HttpStatus.BAD_REQUEST, "지원하지 않는 소셜 플랫폼입니다"),

  // 404
  NOT_FOUND_PHONE_VERIFICATION(HttpStatus.NOT_FOUND, "존재하지 않는 번호 인증 이력입니다."),
  ;
  private final HttpStatus status;
  private final String message;
}
