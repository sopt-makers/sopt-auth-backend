package sopt.makers.authentication.external.oauth;

import sopt.makers.authentication.external.oauth.dto.IdTokenResponse;

public interface OAuthService {
  IdTokenResponse getIdTokenByCode(String code);
}
