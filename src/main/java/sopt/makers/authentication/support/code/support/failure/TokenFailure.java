package sopt.makers.authentication.support.code.support.failure;

import static lombok.AccessLevel.PRIVATE;

import sopt.makers.authentication.support.code.base.FailureCode;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public enum TokenFailure implements FailureCode {
  TOKEN_EXPIRED(HttpStatus.BAD_REQUEST, "토큰이 만료되었습니다."),
  UNSUPPORTED_ISSUER(HttpStatus.BAD_REQUEST, "신뢰할 수 없는 발급자입니다."),
  INVALID_SUBJECT(HttpStatus.BAD_REQUEST, "주체 정보가 잘못되었습니다."),
  INVALID_PREFIX(HttpStatus.BAD_REQUEST, "토큰 접두사가 잘못되었습니다."),
  INVALID_ALGORITHM(HttpStatus.BAD_REQUEST, "알고리즘이 잘못되었습니다."),
  INVALID_SIGNATURE(HttpStatus.BAD_REQUEST, "서명이 잘못되었습니다."),
  INVALID_LOCATION(HttpStatus.BAD_REQUEST, "키 파일 위치가 잘못되었습니다."),
  ;
  ;
  private final HttpStatus status;
  private final String message;
}
