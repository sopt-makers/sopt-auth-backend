package sopt.makers.authentication.adapter.in.web.controller.auth;

import org.springframework.http.ResponseEntity;

public interface AuthKeyApi {

  ResponseEntity<?> retrievePublicJwks();
}
