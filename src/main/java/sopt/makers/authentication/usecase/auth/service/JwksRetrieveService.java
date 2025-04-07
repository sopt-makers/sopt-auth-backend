package sopt.makers.authentication.usecase.auth.service;

import static com.nimbusds.jose.JWSAlgorithm.RS512;
import static com.nimbusds.jose.jwk.KeyUse.SIGNATURE;

import sopt.makers.authentication.support.jwt.RSAKeyManager;
import sopt.makers.authentication.support.value.*;
import sopt.makers.authentication.usecase.auth.port.in.JwksRetrieveUsecase;

import org.springframework.stereotype.Service;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwksRetrieveService implements JwksRetrieveUsecase {

  private final RSAKeyManager rsaKeyManager;
  private final SecurityProperty securityProperty;

  public JWKSet retrievePublicKey() {

    RSAKey jwk =
        new RSAKey.Builder(rsaKeyManager.getPublicKey())
            .keyUse(SIGNATURE)
            .algorithm(RS512)
            .keyID(securityProperty.jwt().secret().rsa().keyId())
            .build();
    return new JWKSet(jwk);
  }
}
