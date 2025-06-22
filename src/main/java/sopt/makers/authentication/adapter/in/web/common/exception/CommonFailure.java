package sopt.makers.authentication.adapter.in.web.common.exception;

import static lombok.AccessLevel.PRIVATE;

import sopt.makers.authentication.common.code.FailureCode;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public enum CommonFailure implements FailureCode {
  // 500
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다"),

  // 400
  METHOD_NOT_SUPPORTED(HttpStatus.BAD_REQUEST, "허용되지 않은 메서드입니다"),
  MISSING_REQUEST_HEADER(HttpStatus.BAD_REQUEST, "필수 요청 헤더가 누락되었습니다"),
  INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "유효하지 않은 입력 값입니다"),
  METHOD_ARGUMENT_TYPE_MISMATCH(HttpStatus.BAD_REQUEST, "입력한 값의 타입이 잘못되었습니다"),
  INVALID_REQUEST_BODY(HttpStatus.BAD_REQUEST, "요청 본문을 읽을 수 없습니다"),

  // 404
  NOT_FOUND_URL(HttpStatus.NOT_FOUND, "존재하지 않는 URL입니다"),
  NO_RESOURCE_FOUND(HttpStatus.NOT_FOUND, "요청한 리소스를 찾을 수 없습니다");

  private final HttpStatus status;
  private final String message;
}
