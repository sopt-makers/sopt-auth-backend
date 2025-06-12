package sopt.makers.authentication.application.auth.api;

import org.springframework.http.ResponseEntity;

public interface AuthKeyApi {

  ResponseEntity<?> retrievePublicJwks();
}
