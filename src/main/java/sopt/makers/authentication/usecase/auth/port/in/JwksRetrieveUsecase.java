package sopt.makers.authentication.usecase.auth.port.in;

import com.nimbusds.jose.jwk.JWKSet;

public interface JwksRetrieveUsecase {

  JWKSet retrievePublicKey();
}
