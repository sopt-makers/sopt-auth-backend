package sopt.makers.authentication.usecase.auth.port.in;

import sopt.makers.authentication.domain.auth.AuthPlatform;

public interface UpdateSocialAccountUsecase {
  boolean update(UpdateSocialAccountCommand command);

  record UpdateSocialAccountCommand(String phone, String token, AuthPlatform authPlatform) {
    public static UpdateSocialAccountCommand of(
        String phone, String token, AuthPlatform authPlatform) {
      return new UpdateSocialAccountCommand(phone, token, authPlatform);
    }
  }
}
