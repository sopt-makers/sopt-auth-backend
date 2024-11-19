package sopt.makers.authentication.support.constant;

public abstract class OAuthConstant {
  public static final String CLIENT_ID = "client_id";
  public static final String CLIENT_SECRET = "client_secret";
  public static final String CODE = "code";
  public static final String GRANT_TYPE = "grant_type";
  public static final String CONTENT_TYPE = "Content-Type";
  public static final String ACCEPT = "Accept";
  public static final String GRANT_TYPE_VALUE = "authorization_code";
  public static final String CONTENT_TYPE_VALUE = "application/x-www-form-urlencoded";
  public static final String ACCEPT_VALUE = "application/json";
  public static final String REDIRECT_URI = "redirect_uri";
  public static final String APPLE_TOKEN_URL = "https://appleid.apple.com/auth/token";
  public static final String GOOGLE_TOKEN_URL = "https://oauth2.googleapis.com/token";
  public static final String APPLE_KEY_ID_HEADER = "kid";
  public static final String APPLE_ALGORITHM_HEADER = "alg";
  public static final String APPLE_ALGORITHM_VALUE = "ES256";
}
