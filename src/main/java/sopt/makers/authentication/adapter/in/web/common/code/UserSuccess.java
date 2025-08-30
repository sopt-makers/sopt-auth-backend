package sopt.makers.authentication.adapter.in.web.common.code;

import static lombok.AccessLevel.PRIVATE;

import sopt.makers.authentication.common.code.SuccessCode;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public enum UserSuccess implements SuccessCode {
  GET_USER_PROFILE(HttpStatus.OK, "유저 기본 정보 조회에 성공했습니다."),
  UPDATE_USER_PROFILE(HttpStatus.OK, "유저 기본 정보 수정에 성공했습니다."),
  GET_USER_COUNT(HttpStatus.OK, "유저 수 조회에 성공했습니다."),

  // 204
  WITHDRAW_USER(HttpStatus.NO_CONTENT, "회원 탈퇴에 성공했습니다. ");

  private final HttpStatus status;
  private final String message;
}
