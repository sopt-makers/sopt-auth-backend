package sopt.makers.authentication.adapter.out.external.sms;

import static lombok.AccessLevel.PRIVATE;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = PRIVATE)
public final class SmsProxyConstant {
  public static final String FORM_DATA_NAME_PHONE = "phone";
  public static final String FORM_DATA_NAME_MESSAGE = "message";
}
