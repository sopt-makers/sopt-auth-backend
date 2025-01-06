package sopt.makers.authentication.usecase.auth.port.in;

public interface GetSocialAccountUsecase {

  SocialAccountPlatformInfo getSocialAccountPlatform(GetSocialAccountPlatformCommand command);

  record GetSocialAccountPlatformCommand(String name, String phone) {}

  record SocialAccountPlatformInfo(String platformName) {}
}
