package sopt.makers.authentication.usecase.auth.port.out;

public interface OAuthAuthenticator {
  String getAuthPlatformId(String authPlatform, String code);
}
