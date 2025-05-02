package sopt.makers.authentication.support.code.domain.failure;

import static lombok.AccessLevel.PRIVATE;

import sopt.makers.authentication.support.code.base.FailureCode;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public enum UserFailure implements FailureCode {
  // 400
  DUPLICATE_ACTIVITY(HttpStatus.BAD_REQUEST, "이미 존재하는 활동 정보입니다"),
  ROLE_REQUIRES_PART(HttpStatus.BAD_REQUEST, "해당 Role은 Part가 필수입니다"),
  NOT_VALID_USER_ID(HttpStatus.BAD_REQUEST, "유저 ID는 1 이상이어야 합니다."),
  // 404
  NOT_FOUND_ROLE(HttpStatus.NOT_FOUND, "존재하지 않는 역할입니다"),
  NOT_FOUND_PART(HttpStatus.NOT_FOUND, "존재하지 않는 파트입니다"),
  NOT_FOUND_USER(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다.");
  private final HttpStatus status;
  private final String message;
}
