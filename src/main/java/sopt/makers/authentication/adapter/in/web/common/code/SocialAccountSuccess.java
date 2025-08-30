package sopt.makers.authentication.adapter.in.web.common.code;

import static lombok.AccessLevel.PRIVATE;

import sopt.makers.authentication.common.code.SuccessCode;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public enum SocialAccountSuccess implements SuccessCode {
  GET_SOCIAL_ACCOUNT_PLATFORM(HttpStatus.OK, "가입 계정 플랫폼 정보 조회에 성공했습니다."),
  UPDATE_SOCIAL_ACCOUNT(HttpStatus.OK, "소셜 계정 변경에 성공했습니다.");

  private final HttpStatus status;
  private final String message;
}
