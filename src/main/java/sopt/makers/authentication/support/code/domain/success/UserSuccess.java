package sopt.makers.authentication.support.code.domain.success;

import static lombok.AccessLevel.PRIVATE;

import sopt.makers.authentication.support.code.base.SuccessCode;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public enum UserSuccess implements SuccessCode {
  GET_USER_INFORMATION(HttpStatus.OK, "유저 기본 정보 조회에 성공했습니다.");

  private final HttpStatus status;
  private final String message;
}
