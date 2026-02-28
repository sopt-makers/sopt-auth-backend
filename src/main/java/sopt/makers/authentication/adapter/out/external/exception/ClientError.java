package sopt.makers.authentication.adapter.out.external.exception;

import static lombok.AccessLevel.PRIVATE;

import sopt.makers.authentication.common.code.FailureCode;

import org.springframework.http.HttpStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = PRIVATE)
public enum ClientError implements FailureCode {
  // 400 BAD_REQUEST
  APPLE_REQUEST_FAIL(HttpStatus.BAD_REQUEST, "Apple 요청에 실패했습니다."),
  GOOGLE_REQUEST_FAIL(HttpStatus.BAD_REQUEST, "Google 요청에 실패했습니다."),
  INVALID_APPLE_REQUEST_URL(HttpStatus.BAD_REQUEST, "유효하지 않은 Apple 요청 URL입니다."),
  INVALID_APPLE_AUTH_CODE(HttpStatus.BAD_REQUEST, "유효하지 않은 Apple 인증 token 입니다."),
  INVALID_GOOGLE_REQUEST_URL(HttpStatus.BAD_REQUEST, "유효하지 않은 Apple 요청 URL입니다."),
  INVALID_GOOGLE_AUTH_CODE(HttpStatus.BAD_REQUEST, "유효하지 않은 Google 인증 token 입니다"),

  // 500 INTERNAL_SERVER_ERROR
  INVALID_ID_TOKEN(HttpStatus.INTERNAL_SERVER_ERROR, "유효하지 않은 id token 입니다."),
  GOOGLE_RESPONSE_UNAVAILABLE(HttpStatus.INTERNAL_SERVER_ERROR, "GOOGLE 인증 요청 결과 반환된 값이 없습니다."),
  APPLE_RESPONSE_UNAVAILABLE(HttpStatus.INTERNAL_SERVER_ERROR, "APPLE 인증 요청 결과 반환된 값이 없습니다."),
  FAIL_READ_APPLE_PRIVATE_KEY_FILE(
      HttpStatus.INTERNAL_SERVER_ERROR, "Apple private key 파일을 읽는데 실패했습니다."),
  APPLE_INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Apple 인증 서버로부터 응답을 받지 못했습니다."),
  PLAYGROUND_REQUEST_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "Playground 요청에 실패했습니다."),
  PLAYGROUND_RESPONSE_UNAVAILABLE(
      HttpStatus.INTERNAL_SERVER_ERROR, "Playground 요청 결과 내 의도한 데이터가 없습니다."),
  APP_REQUEST_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "APP 요청에 실패했습니다."),
  APP_RESPONSE_UNAVAILABLE(HttpStatus.INTERNAL_SERVER_ERROR, "APP 요청 결과 내 의도한 데이터가 없습니다."),
  S3_REQUEST_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "S3 요청에 실패했습니다."),
  S3_RESPONSE_UNAVAILABLE(HttpStatus.INTERNAL_SERVER_ERROR, "S3 파일 다운로드에 실패했습니다."),
  SMS_PROXY_REQUEST_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "Internal SMS Proxy 요청에 실패했습니다.");

  private final HttpStatus status;
  private final String message;
  private String additionalMessage;

  @Override
  public HttpStatus getStatus() {
    return this.status;
  }

  @Override
  public String getMessage() {
    return additionalMessage == null
        ? this.message
        : String.format("%s (response message = %s)", this.message, this.additionalMessage);
  }

  public void addMessage(String message) {
    this.additionalMessage = message;
  }
}
