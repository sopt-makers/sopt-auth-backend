package sopt.makers.authentication.application.auth.dto.request;

import static lombok.AccessLevel.*;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = PRIVATE)
public final class AuthRequest {

  public record CreatePhoneVerification(String name, String number) {}

  public record VerifyPhoneVerification(String name, String number, String code) {}
}
