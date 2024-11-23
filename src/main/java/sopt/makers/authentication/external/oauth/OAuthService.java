package sopt.makers.authentication.external.oauth;

import sopt.makers.authentication.external.oauth.dto.IdTokenResponse;

public interface OAuthService { // OAuth
  IdTokenResponse getIdTokenByCode(String code);

  // 클라이언트를 조합해서 사용하는 애들이 유즈케이스를 상속받은 애들은 service 패키지
}
