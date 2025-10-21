package sopt.makers.authentication.common.constant;

public final class CookieConstant {
  private CookieConstant() {}

  public static final String SAME_SITE_NONE = "None";
  public static final String COOKIE_DOMAIN = ".sopt.org";
  public static final String[] CORS_ALLOWED_ORIGINS = {
    "http://localhost:3000",
    "http://localhost:5173",
    "https://sopt-internal-dev.sopt.org",
    "https://playground.sopt.org"
  };
}
