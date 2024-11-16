package sopt.makers.authentication.external.oauth;

public interface OAuthService {
  String getAuthPlatformId(String code);
}
