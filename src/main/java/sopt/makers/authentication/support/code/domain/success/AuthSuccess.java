package sopt.makers.authentication.support.code.domain.success;

import static lombok.AccessLevel.PRIVATE;

import sopt.makers.authentication.support.code.base.SuccessCode;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public enum AuthSuccess implements SuccessCode {
  VERIFY_PHONE_VERIFICATION(HttpStatus.OK, "번호 인증에 성공했습니다."),

  CREATE_PHONE_VERIFICATION(HttpStatus.CREATED, "번호 인증 생성에 성공했습니다."),
  ;

  private final HttpStatus status;
  private final String message;
}
