package sopt.makers.authentication.support.constant;

import java.util.List;

public final class SystemConstant {
  private SystemConstant() {}

  public static final String UTF_8 = "UTF-8";
  private static final String API_PATH_PREFIX = "/api";
  private static final String API_VERSION = "/v1";

  public static final String API_DEFAULT_PREFIX = API_PATH_PREFIX + API_VERSION;

  private static final String PATH_ACTUATOR = "/actuator";
  private static final String PATH_AUTH = API_DEFAULT_PREFIX + "/auth";
  private static final String PATH_ERROR = "/error";
  private static final String PATH_TEST = "/test";
  private static final String PATH_GET_REGISTER_SOCIAL_PLATFORM =
      API_PATH_PREFIX + "/social/accounts/social";

  public static List<String> WHITE_PATHS =
      List.of(PATH_ACTUATOR, PATH_AUTH, PATH_GET_REGISTER_SOCIAL_PLATFORM, PATH_ERROR, PATH_TEST);

  public static final String PATTERN_ALL = "/**";
  public static final String PATTERN_ERROR_PATH = PATH_ERROR + PATTERN_ALL;
  public static final String PATTERN_ACTUATOR = PATH_ACTUATOR + PATTERN_ALL;
  public static final String PATTERN_AUTH = PATH_AUTH + PATTERN_ALL;
  public static final String PATTERN_TEST = API_DEFAULT_PREFIX + PATH_TEST + PATTERN_ALL;
  public static final String PATTERN_ROOT_PATH = "/";
}
