package sopt.makers.authentication.support.code.domain.success;

import static lombok.AccessLevel.PRIVATE;

import sopt.makers.authentication.support.code.base.*;

import org.springframework.http.*;

import lombok.*;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public enum SocialAccountSuccess implements SuccessCode {
  UPDATE_SOCIAL_ACCOUNT(HttpStatus.OK, "소셜 계정 변경에 성공했습니다.");

  private final HttpStatus status;
  private final String message;
}
