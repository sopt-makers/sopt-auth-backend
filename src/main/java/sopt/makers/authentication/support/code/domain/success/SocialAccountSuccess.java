package sopt.makers.authentication.support.code.domain.success;

import static lombok.AccessLevel.PRIVATE;

import sopt.makers.authentication.support.code.base.*;

import org.springframework.http.*;

import lombok.*;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public enum SocialAccountSuccess implements SuccessCode {
  GET_SOCIAL_ACCOUNT_PLATFORM(HttpStatus.OK, "가입 계정 플랫폼 정보 조회에 성공했습니다."),
  UPDATE_SOCIAL_ACCOUNT(HttpStatus.OK, "소셜 계정 변경에 성공했습니다.");

  private final HttpStatus status;
  private final String message;
}
