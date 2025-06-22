package sopt.makers.authentication.application.port.in.auth;

public interface GetSocialAccountUsecase {

  SocialAccountPlatformInfo getSocialAccountPlatform(GetSocialAccountPlatformCommand command);

  record GetSocialAccountPlatformCommand(String name, String phone) {}

  record SocialAccountPlatformInfo(String platformName) {}
}
