package sopt.makers.authentication.adapter.out.jwt.exception;

import static lombok.AccessLevel.PRIVATE;

import sopt.makers.authentication.common.code.FailureCode;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public enum ResourceFailure implements FailureCode {
  INVALID_LOCATION(HttpStatus.BAD_REQUEST, "키 파일 위치가 잘못되었습니다."),
  INVALID_SUBJECT(HttpStatus.BAD_REQUEST, "주체 정보가 잘못되었습니다."),
  INVALID_ALGORITHM(HttpStatus.BAD_REQUEST, "알고리즘이 잘못되었습니다."),
  ;
  private final HttpStatus status;
  private final String message;
}
