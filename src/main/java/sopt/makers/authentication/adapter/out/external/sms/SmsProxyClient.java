package sopt.makers.authentication.adapter.out.external.sms;

import static sopt.makers.authentication.adapter.out.external.exception.ClientError.SMS_PROXY_REQUEST_FAIL;
import static sopt.makers.authentication.adapter.out.external.sms.SmsProxyConstant.FORM_DATA_NAME_MESSAGE;
import static sopt.makers.authentication.adapter.out.external.sms.SmsProxyConstant.FORM_DATA_NAME_PHONE;
import static sopt.makers.authentication.common.constant.SystemConstant.API_KEY_HEADER;
import static sopt.makers.authentication.common.constant.SystemConstant.JSON;

import sopt.makers.authentication.adapter.out.external.exception.ClientException.ClientRequestException;
import sopt.makers.authentication.config.ExternalProperty;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class SmsProxyClient {
  private final OkHttpClient smsOkHttpClient;
  private final ExternalProperty externalProperty;

  public void sendSMS(String phone, String message) {
    int retryCount = 0;

    while (retryCount < 3) {
      try {
        sendSmsToProxyServer(phone, message);
        log.info("SMS 발송 성공 - 수신자: {}", phone);
        return;
      } catch (Exception e) {
        retryCount++;
        log.warn("SMS 발송 실패 - 재시도 {}/3, 수신자: {}, 에러: {}", retryCount, phone, e.getMessage());
      }
    }

    throw new ClientRequestException(SMS_PROXY_REQUEST_FAIL);
  }

  private void sendSmsToProxyServer(String phone, String message) {
    String json =
        new Gson().toJson(Map.of(FORM_DATA_NAME_PHONE, phone, FORM_DATA_NAME_MESSAGE, message));

    RequestBody body = RequestBody.create(json, JSON);

    Request request =
        new Request.Builder()
            .url(externalProperty.sms().internal().url())
            .post(body)
            .addHeader(API_KEY_HEADER, externalProperty.sms().internal().apiKey())
            .build();

    try (Response response = smsOkHttpClient.newCall(request).execute()) {
      String responseBody = response.body() != null ? response.body().string() : "empty";

      if (!response.isSuccessful()) {
        log.error("Internal SMS 서버 응답 실패 - status: {}, body: {}", response.code(), responseBody);
        throw new ClientRequestException(SMS_PROXY_REQUEST_FAIL);
      }

      log.debug("Internal SMS 서버 응답 - body: {}", responseBody);
    } catch (IOException e) {
      log.error("Internal SMS 서버 통신 실패 - 수신자: {}", phone, e);
      throw new ClientRequestException(SMS_PROXY_REQUEST_FAIL);
    }
  }
}
