package sopt.makers.authentication.support.value;

public final class SystemConst {

    private SystemConst() {}

    public static final String UTF_8 = "UTF-8";
    private static final String API_PATH_PREFIX = "/api";
    private static final String API_VERSION = "/v1";

    public static final String API_DEFAULT_PREFIX = API_PATH_PREFIX + API_VERSION;

    public static final String PATTERN_ALL = "/**";
    public static final String PATTERN_ERROR_PATH = "/error";
    public static final String PATTERN_SWAGGER = "/swagger-ui/**";
    public static final String PATTERN_AUTH = API_DEFAULT_PREFIX + "/auth" + PATTERN_ALL;
    public static final String PATTERN_TEST = API_DEFAULT_PREFIX + "/test" + PATTERN_ALL;

}
