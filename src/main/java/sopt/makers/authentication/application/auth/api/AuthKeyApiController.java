package sopt.makers.authentication.application.auth.api;

import sopt.makers.authentication.usecase.auth.port.in.JwksRetrieveUsecase;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/.well-known")
@RequiredArgsConstructor
public class AuthKeyApiController implements AuthKeyApi {

  private final JwksRetrieveUsecase jwksRetrieveUsecase;

  @Override
  @GetMapping(value = "/jwks.json")
  public ResponseEntity<?> retrievePublicJwks() {
    return ResponseEntity.status(HttpStatus.OK).body(jwksRetrieveUsecase.retrievePublicKey());
  }
}
