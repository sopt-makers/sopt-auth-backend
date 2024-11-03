package sopt.makers.authentication.application.auth.dto.request;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class Create {

  public record PhoneVerification(String name, String number) {}
}
