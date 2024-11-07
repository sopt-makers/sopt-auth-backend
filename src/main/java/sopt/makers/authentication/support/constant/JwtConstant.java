package sopt.makers.authentication.support.constant;

public class JwtConstant {

  public static final String[] ISSUERS = {"operation", "playground"};
  public static final String ISSUER = "operation";

  public static final long ACCESS_TOKEN_EXPIRATION = 1000L * 60 * 10;
  public static final long REFRESH_TOKEN_EXPIRATION = 1000L * 60 * 60 * 24 * 7;
  public static final String TOKEN_HEADER = "Bearer ";
}
