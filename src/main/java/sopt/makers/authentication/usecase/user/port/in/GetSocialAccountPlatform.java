package sopt.makers.authentication.usecase.user.port.in;

public interface GetSocialAccountPlatform {

  SocialAccountPlatformInfo getSocialAccountPlatform(GetSocialAccountPlatformCommand command);

  record GetSocialAccountPlatformCommand(String name, String phone) {}

  record SocialAccountPlatformInfo(String platformName) {}
}
