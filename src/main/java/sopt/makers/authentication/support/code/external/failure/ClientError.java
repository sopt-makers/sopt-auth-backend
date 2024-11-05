package sopt.makers.authentication.support.code.external.failure;

import static lombok.AccessLevel.*;

import sopt.makers.authentication.support.code.base.FailureCode;

import org.springframework.http.HttpStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = PRIVATE)
public enum ClientError implements FailureCode {
  GABIA_REQUEST_INVALID_AUTH_DATA(HttpStatus.BAD_REQUEST, "Gabia에 잘못된 인증 데이터가 전달됐습니다."),
  GABIA_REQUEST_FAIL(HttpStatus.BAD_REQUEST, "Gabia 요청에 실패했습니다."),
  GABIA_RESPONSE_UNAVAILABLE(HttpStatus.BAD_REQUEST, "Gabia 요청 결과 내 의도한 데이터가 없습니다."),
  GABIA_RESPONSE_BIND_FAIL(HttpStatus.BAD_REQUEST, "Gabia 요청 결과 역질렬화에 실패했습니다."),
  ;

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
