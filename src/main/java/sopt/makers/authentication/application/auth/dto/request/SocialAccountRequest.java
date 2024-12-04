package sopt.makers.authentication.application.auth.dto.request;

import static lombok.AccessLevel.PRIVATE;

import sopt.makers.authentication.domain.auth.AuthPlatform;
import sopt.makers.authentication.usecase.auth.port.in.UpdateSocialAccountUsecase.UpdateSocialAccountCommand;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = PRIVATE)
public final class SocialAccountRequest {
  public record UpdateSocialAccount(String phone, String code, String authPlatform) {
    public UpdateSocialAccountCommand toCommand() {
      return UpdateSocialAccountCommand.of(phone, AuthPlatform.find(authPlatform), code);
    }
  }
}
