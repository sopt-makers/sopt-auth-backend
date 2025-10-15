package sopt.makers.authentication.domain.auth.exception;

import static lombok.AccessLevel.PRIVATE;

import sopt.makers.authentication.common.code.FailureCode;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public enum AuthFailure implements FailureCode {
  // 400
  INVALID_SOCIAL_PLATFORM(HttpStatus.BAD_REQUEST, "지원하지 않는 소셜 플랫폼입니다."),
  INVALID_ID_TOKEN(HttpStatus.BAD_REQUEST, "ID 토큰 유효성 검사에 실패했습니다."),
  INVALID_PHONE_VERIFICATION_CODE(HttpStatus.BAD_REQUEST, "인증번호가 일치하지 않습니다."),
  ALREADY_REGISTER_PHONE_NUMBER(HttpStatus.BAD_REQUEST, "이미 가입된 전화번호입니다."),
  EXPIRED_PHONE_VERIFICATION(HttpStatus.BAD_REQUEST, "인증 시간이 만료되었습니다."),

  // 401
  INVALID_API_KEY(HttpStatus.UNAUTHORIZED, "API 키가 유효하지 않습니다."),
  MISSING_AUTHORIZATION_HEADER(HttpStatus.UNAUTHORIZED, "Authorization 헤더가 없습니다."),
  REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "Refresh Token이 없습니다."),

  // 403
  PHONE_NOT_VERIFIED(HttpStatus.FORBIDDEN, "번호 인증이 되지 않은 번호입니다."),

  // 404
  NOT_FOUND_PHONE_VERIFICATION(HttpStatus.NOT_FOUND, "존재하지 않는 번호 인증 이력입니다."),
  NOT_FOUND_USER_WITH_SOCIAL_ACCOUNT(HttpStatus.BAD_REQUEST, "소셜 계정 정보와 일치하는 회원이 없습니다."),
  NOT_FOUND_AVAILABLE_PUBLIC_KEY_SET(HttpStatus.NOT_FOUND, "유효한 Public Key Set를 찾을 수 없습니다."),
  NOT_FOUND_REGISTER_INFO(HttpStatus.NOT_FOUND, "SOPT 활동 시 사용한 전화번호가 아니에요."),

  // 500
  APP_SYNC_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "회원가입 실패 - App 서버 동기화에 실패했습니다."),
  PLAYGROUND_SYNC_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "회원가입 실패 - Playground 서버 동기화에 실패했습니다.");

  private final HttpStatus status;
  private final String message;
}
