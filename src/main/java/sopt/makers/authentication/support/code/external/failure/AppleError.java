package sopt.makers.authentication.support.code.external.failure;

import sopt.makers.authentication.support.code.base.*;

import org.springframework.http.*;

import lombok.*;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum AppleError implements FailureCode {
  FAIL_READ_APPLE_PRIVATE_KEY_FILE(
      HttpStatus.INTERNAL_SERVER_ERROR, "Apple private key 파일을 읽는데 실패했습니다."),
  APPLE_INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Apple 인증 서버로부터 응답을 받지 못했습니다."),
  FAIL_APPLE_AUTH_REQUEST(HttpStatus.BAD_REQUEST, "Apple 인증 요청에 실패했습니다."),
  INVALID_APPLE_AUTH_CODE(HttpStatus.BAD_REQUEST, "유효하지 않은 Apple 인증 code 입니다.");
  private final HttpStatus status;
  private final String message;

  @Override
  public HttpStatus getStatus() {
    return status;
  }

  @Override
  public String getMessage() {
    return message;
  }
}
