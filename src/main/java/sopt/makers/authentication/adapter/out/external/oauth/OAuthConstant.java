package sopt.makers.authentication.adapter.out.external.oauth;

import static lombok.AccessLevel.PRIVATE;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = PRIVATE)
public final class OAuthConstant {
  public static final String APPLE_PUBLIC_KEY_SET_URL = "https://appleid.apple.com/auth/keys";
  public static final String GOOGLE_PUBLIC_KEY_SET_URL =
      "https://www.googleapis.com/oauth2/v3/certs";
  public static final String APPLE_ISSUER = "https://appleid.apple.com";
  public static final String GOOGLE_ISSUER = "https://accounts.google.com";
}
