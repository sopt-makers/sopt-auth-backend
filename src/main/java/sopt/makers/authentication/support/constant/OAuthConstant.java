package sopt.makers.authentication.support.constant;

public abstract class OAuthConstant {
  public static final String GRANT_TYPE = "authorization_code";
  public static final String CONTENT_TYPE = "application/x-www-form-urlencoded";
  public static final String ACCEPT = "application/json";
  public static final int APPLE_TOKEN_EXPIRATION_TIME = 3600 * 1000; // 1 hour
  public static final String APPLE_TOKEN_URL = "https://appleid.apple.com/auth/token";
  public static final String GOOGLE_TOKEN_URL = "https://oauth2.googleapis.com/token";
}
