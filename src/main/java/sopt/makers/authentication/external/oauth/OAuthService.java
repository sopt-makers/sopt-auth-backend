package sopt.makers.authentication.external.oauth;

public interface OAuthService {
  String getIdentifierByToken(String token);
}
