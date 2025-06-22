package sopt.makers.authentication.adapter.out.external.sms;

import static lombok.AccessLevel.PRIVATE;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = PRIVATE)
public final class GabiaConstant {

  public static final String AUTHORIZATION_PREFIX = "Basic ";
  public static final String FORMAT_AUTHORIZATION = "%s:%s";

  public static final String URI_OAUTH_TOKEN = "/oauth/token";
  public static final String URI_SEND_SMS = "/api/send/sms";
  public static final String URI_SEND_LMS = "/api/send/lms";

  public static final String FORM_DATA_NAME_GRANT_TYPE = "grant_type";
  public static final String FORM_DATA_NAME_PHONE = "phone";
  public static final String FORM_DATA_NAME_CALLBACK = "callback";
  public static final String FORM_DATA_NAME_SUBJECT = "subject";
  public static final String FORM_DATA_NAME_MESSAGE = "message";
  public static final String FORM_DATA_NAME_REFERENCE_KEY = "refkey";

  public static final String FORM_DATA_VALUE_GRANT_TYPE = "client_credentials";

  public static final String RESPONSE_ACCESS_TOKEN_FIELD = "access_token";

  public static final String RESPONSE_SUCCESS_FLAG_FIELD = "message";
  public static final String RESPONSE_SUCCESS_FLAG_VALUE = "Success";
}
