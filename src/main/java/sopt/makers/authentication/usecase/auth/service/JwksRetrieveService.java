package sopt.makers.authentication.usecase.auth.service;

import sopt.makers.authentication.support.jwt.RSAKeyManager;
import sopt.makers.authentication.usecase.auth.port.in.JwksRetrieveUsecase;

import org.springframework.stereotype.Service;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwksRetrieveService implements JwksRetrieveUsecase {

  private final RSAKeyManager rsaKeyManager;

  public JWKSet retrievePublicKey() {

    RSAKey jwk =
        new RSAKey.Builder(rsaKeyManager.getPublicKey())
            .keyUse(KeyUse.SIGNATURE)
            .algorithm(JWSAlgorithm.RS512)
            .keyID("makers-auth-1")
            .build();
    return new JWKSet(jwk);
  }
}
