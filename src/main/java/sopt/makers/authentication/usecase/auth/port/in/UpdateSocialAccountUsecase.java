package sopt.makers.authentication.usecase.auth.port.in;

public interface UpdateSocialAccountUsecase {
  boolean update(UpdateSocialAccountCommand command);

  record UpdateSocialAccountCommand(String phone, String authPlatform, String code) {
    public static UpdateSocialAccountCommand of(String phone, String authPlatform, String code) {
      return new UpdateSocialAccountCommand(phone, authPlatform, code);
    }
  }
}
