package sopt.makers.authentication.support.code.support.failure;

import static lombok.AccessLevel.PRIVATE;

import sopt.makers.authentication.support.code.base.*;

import org.springframework.http.*;

import lombok.*;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public enum CommonFailure implements FailureCode {
  INVALID_API_KEY(HttpStatus.FORBIDDEN, "API 키가 유효하지 않습니다"),
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다");
  private final HttpStatus status;
  private final String message;
}
