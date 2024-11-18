package sopt.makers.authentication.application.auth.dto.response;

import static lombok.AccessLevel.PRIVATE;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = PRIVATE)
public final class AuthResponse {

  public record VerifyResult(@JsonProperty("isVerified") boolean isVerified) {}
}
