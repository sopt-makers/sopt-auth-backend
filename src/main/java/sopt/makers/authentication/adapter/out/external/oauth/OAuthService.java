package sopt.makers.authentication.adapter.out.external.oauth;

public interface OAuthService {
  String getIdentifierByToken(String token);
}
