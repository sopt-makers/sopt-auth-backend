package sopt.makers.authentication.common.constant;

import java.util.List;

import okhttp3.MediaType;

public final class SystemConstant {
  private SystemConstant() {}

  public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
  public static final String UTF_8 = "UTF-8";
  public static final String RSA = "RSA";
  private static final String API_PATH_PREFIX = "/api";
  private static final String API_VERSION = "/v1";

  private static final String API_DEFAULT_PREFIX = API_PATH_PREFIX + API_VERSION;
  public static final String PATH_AUTH = API_DEFAULT_PREFIX + "/auth";
  public static final String PATH_SOCIAL_ACCOUNT = API_DEFAULT_PREFIX + "/social/accounts";
  private static final String PATH_ERROR = "/error";
  private static final String PATH_TEST = "/test";
  public static final String PATTERN_ALL = "/**";

  public static final String PATTERN_ERROR_PATH = PATH_ERROR + PATTERN_ALL;
  public static final String PATTERN_AUTH = PATH_AUTH + PATTERN_ALL;
  public static final String PATTERN_SOCIAL_ACCOUNT = PATH_SOCIAL_ACCOUNT + PATTERN_ALL;
  public static final String PATTERN_TEST = API_DEFAULT_PREFIX + PATH_TEST + PATTERN_ALL;
  public static final String PATTERN_ROOT_PATH = "/";

  public static final List<String> WHITELIST_WILDCARD =
      List.of(PATH_ERROR, PATH_AUTH, PATH_SOCIAL_ACCOUNT, PATH_TEST);

  public static final String API_KEY_HEADER = "X-Api-Key";
  public static final String SERVICE_NAME_HEADER = "X-Service-Name";

  public static final String ROLE = "ROLE_";
  public static final String INTERNAL_SERVICE = "INTERNAL_SERVICE";
  public static final String USER_CACHE_NAME = "user";
  public static final String TEMP_DIR_PROPERTY = "java.io.tmpdir";
}
