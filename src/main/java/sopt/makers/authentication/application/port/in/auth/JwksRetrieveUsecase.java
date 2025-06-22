package sopt.makers.authentication.application.port.in.auth;

import com.nimbusds.jose.jwk.JWKSet;

public interface JwksRetrieveUsecase {

  JWKSet retrievePublicKey();
}
