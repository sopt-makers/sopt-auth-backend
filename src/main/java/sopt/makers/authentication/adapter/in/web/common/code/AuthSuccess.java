package sopt.makers.authentication.adapter.in.web.common.code;

import static lombok.AccessLevel.PRIVATE;

import sopt.makers.authentication.common.code.SuccessCode;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public enum AuthSuccess implements SuccessCode {
  // 200
  VERIFY_PHONE_VERIFICATION(HttpStatus.OK, "번호 인증에 성공했습니다."),
  AUTHENTICATE_SOCIAL_ACCOUNT(HttpStatus.OK, "소셜 로그인에 성공했습니다."),
  REFRESH_TOKEN(HttpStatus.OK, "토큰 갱신에 성공했습니다."),

  // 201
  CREATE_PHONE_VERIFICATION(HttpStatus.CREATED, "번호 인증 생성에 성공했습니다."),
  CREATE_SIGN_UP_USER(HttpStatus.CREATED, "회원 가입에 성공했습니다."),
  ;

  private final HttpStatus status;
  private final String message;
}
