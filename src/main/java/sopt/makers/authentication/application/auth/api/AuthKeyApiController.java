package sopt.makers.authentication.application.auth.api;

import sopt.makers.authentication.usecase.auth.port.in.JwksRetrieveUsecase;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nimbusds.jose.jwk.JWKSet;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthKeyApiController implements AuthKeyApi {
  private final JwksRetrieveUsecase jwksRetrieveUsecase;

  @Override
  @GetMapping("${security.api.secured-endpoints[0]}")
  public ResponseEntity<?> retrievePublicJwks() {
    JWKSet jwkSet = jwksRetrieveUsecase.retrievePublicKey();
    return ResponseEntity.status(HttpStatus.OK).body(jwkSet.toJSONObject(true));
  }
}
