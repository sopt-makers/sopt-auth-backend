package sopt.makers.authentication.adapter.out.external.sms;

import static sopt.makers.authentication.adapter.out.external.sms.GabiaConstant.AUTHORIZATION_PREFIX;
import static sopt.makers.authentication.adapter.out.external.sms.GabiaConstant.FORMAT_AUTHORIZATION;
import static sopt.makers.authentication.adapter.out.external.sms.GabiaConstant.FORM_DATA_NAME_CALLBACK;
import static sopt.makers.authentication.adapter.out.external.sms.GabiaConstant.FORM_DATA_NAME_GRANT_TYPE;
import static sopt.makers.authentication.adapter.out.external.sms.GabiaConstant.FORM_DATA_NAME_MESSAGE;
import static sopt.makers.authentication.adapter.out.external.sms.GabiaConstant.FORM_DATA_NAME_PHONE;
import static sopt.makers.authentication.adapter.out.external.sms.GabiaConstant.FORM_DATA_NAME_REFERENCE_KEY;
import static sopt.makers.authentication.adapter.out.external.sms.GabiaConstant.FORM_DATA_NAME_SUBJECT;
import static sopt.makers.authentication.adapter.out.external.sms.GabiaConstant.FORM_DATA_VALUE_GRANT_TYPE;
import static sopt.makers.authentication.adapter.out.external.sms.GabiaConstant.RESPONSE_ACCESS_TOKEN_FIELD;
import static sopt.makers.authentication.adapter.out.external.sms.GabiaConstant.RESPONSE_SUCCESS_FLAG_FIELD;
import static sopt.makers.authentication.adapter.out.external.sms.GabiaConstant.RESPONSE_SUCCESS_FLAG_VALUE;
import static sopt.makers.authentication.adapter.out.external.sms.GabiaConstant.URI_OAUTH_TOKEN;
import static sopt.makers.authentication.adapter.out.external.sms.GabiaConstant.URI_SEND_LMS;
import static sopt.makers.authentication.adapter.out.external.sms.GabiaConstant.URI_SEND_SMS;

import sopt.makers.authentication.adapter.out.external.exception.ClientError;
import sopt.makers.authentication.adapter.out.external.exception.ClientRequestException;
import sopt.makers.authentication.adapter.out.external.exception.ClientResponseException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import lombok.RequiredArgsConstructor;
import okhttp3.Headers;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Component
@RequiredArgsConstructor
class GabiaClient {

  public static final String COLON = " : ";
  private final GabiaProperty gabiaProperty;
  private final Gson gson;
  private final OkHttpClient client;

  protected void sendSmsMessage(String receiver, String content) {
    String authToken = authenticate();

    RequestBody requestBody =
        new MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            // 수신번호가 두 개 이상인 경우 ',' 를 이용하여 입력 ex) 01011112222,01033334444
            .addFormDataPart(FORM_DATA_NAME_PHONE, receiver)
            .addFormDataPart(FORM_DATA_NAME_CALLBACK, gabiaProperty.sms().phone())
            .addFormDataPart(FORM_DATA_NAME_MESSAGE, content)
            .addFormDataPart(FORM_DATA_NAME_REFERENCE_KEY, generateReferenceKey())
            .build();

    Request request = buildPostRequest(URI_SEND_SMS, authToken, requestBody);
    try (Response response = executeRequest(request)) {
      String fieldValue = extractSerializedDataIn(RESPONSE_SUCCESS_FLAG_FIELD, response);
      validateResponseData(fieldValue, RESPONSE_SUCCESS_FLAG_VALUE);
    }
  }

  protected void sendLmsMessage(String receiver, String title, String content) {
    String authToken = authenticate();

    RequestBody requestBody =
        new MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            // 수신번호가 두 개 이상인 경우 ',' 를 이용하여 입력 ex) 01011112222,01033334444
            .addFormDataPart(FORM_DATA_NAME_PHONE, receiver)
            .addFormDataPart(FORM_DATA_NAME_CALLBACK, gabiaProperty.sms().phone())
            .addFormDataPart(FORM_DATA_NAME_MESSAGE, content)
            .addFormDataPart(FORM_DATA_NAME_REFERENCE_KEY, generateReferenceKey())
            .addFormDataPart(FORM_DATA_NAME_SUBJECT, title)
            .build();

    Request request = buildPostRequest(URI_SEND_LMS, authToken, requestBody);
    try (Response response = executeRequest(request)) {
      String fieldValue = extractSerializedDataIn(RESPONSE_SUCCESS_FLAG_FIELD, response);
      validateResponseData(fieldValue, RESPONSE_SUCCESS_FLAG_VALUE);
    }
  }

  private String authenticate() {
    RequestBody requestBody =
        new MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(FORM_DATA_NAME_GRANT_TYPE, FORM_DATA_VALUE_GRANT_TYPE)
            .build();

    Request request = buildPostRequest(URI_OAUTH_TOKEN, gabiaProperty.sms().key(), requestBody);
    try (Response response = executeRequest(request)) {
      String authValue = extractSerializedDataIn(RESPONSE_ACCESS_TOKEN_FIELD, response);
      validateResponseData(authValue);
      return authValue;
    }
  }

  private String extractSerializedDataIn(String dataKey, Response response) {
    try {
      HashMap<String, String> result =
          gson.fromJson(Objects.requireNonNull(response.body()).string(), HashMap.class);
      return result.get(dataKey);
    } catch (IOException | JsonSyntaxException | JsonIOException e) {
      throw new ClientResponseException(ClientError.GABIA_RESPONSE_BIND_FAIL);
    }
  }

  private void validateResponseData(String data) {
    if (Objects.isNull(data)) {
      ClientError error = ClientError.GABIA_REQUEST_INVALID_AUTH_DATA;
      error.addMessage(String.join(COLON, RESPONSE_ACCESS_TOKEN_FIELD, null));
      throw new ClientRequestException(error);
    }
  }

  private void validateResponseData(String data, String dataExpected) {
    boolean isExpectData = data.equals(dataExpected);
    if (!isExpectData) {
      ClientError error = ClientError.GABIA_REQUEST_INVALID_AUTH_DATA;
      error.addMessage(String.join(COLON, RESPONSE_ACCESS_TOKEN_FIELD, data));
      throw new ClientRequestException(error);
    }
  }

  private String generateReferenceKey() {
    return UUID.randomUUID().toString();
  }

  private Request buildPostRequest(
      String requestUri, String authorizationValue, RequestBody requestBody) {
    return new Request.Builder()
        .url(gabiaProperty.sms().url() + requestUri)
        .post(requestBody)
        .headers(generateHeaderOfAuthorization(authorizationValue))
        .build();
  }

  private Headers generateHeaderOfAuthorization(String authValue) {
    Map<String, String> headers = new HashMap<>();
    headers.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
    headers.put(HttpHeaders.AUTHORIZATION, AUTHORIZATION_PREFIX + encodeAuthorization(authValue));
    headers.put(HttpHeaders.CACHE_CONTROL, CacheControl.noCache().getHeaderValue());
    return Headers.of(headers);
  }

  private String encodeAuthorization(String value) {
    return Base64.getEncoder()
        .encodeToString(
            String.format(FORMAT_AUTHORIZATION, gabiaProperty.sms().id(), value)
                .getBytes(StandardCharsets.UTF_8));
  }

  private Response executeRequest(Request request) {
    try {
      return client.newCall(request).execute();
    } catch (IOException e) {
      throw new ClientRequestException(ClientError.GABIA_REQUEST_INVALID_AUTH_DATA);
    }
  }
}
