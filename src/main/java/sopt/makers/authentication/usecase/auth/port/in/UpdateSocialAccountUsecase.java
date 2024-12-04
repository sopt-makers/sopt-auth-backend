package sopt.makers.authentication.usecase.auth.port.in;

import sopt.makers.authentication.domain.auth.AuthPlatform;

public interface UpdateSocialAccountUsecase {
  boolean update(UpdateSocialAccountCommand command);

  record UpdateSocialAccountCommand(String phone, AuthPlatform authPlatform, String code) {
    public static UpdateSocialAccountCommand of(
        String phone, AuthPlatform authPlatform, String code) {
      return new UpdateSocialAccountCommand(phone, authPlatform, code);
    }
  }
}
