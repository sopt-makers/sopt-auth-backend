package sopt.makers.authentication.external.gabia;

import sopt.makers.authentication.support.code.external.failure.ClientError;
import sopt.makers.authentication.support.exception.external.ClientRequestException;
import sopt.makers.authentication.support.exception.external.ClientResponseException;
import sopt.makers.authentication.support.value.GabiaProperty;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import lombok.RequiredArgsConstructor;
import okhttp3.*;

@Component
@RequiredArgsConstructor
class GabiaClient {

  private static final String FORMAT_AUTHORIZATION = "%s:%s";
  private static final String URI_OAUTH_TOKEN = "/oauth/token";
  private static final String URI_SEND_SMS = "/api/send/sms";
  private static final String URI_SEND_LMS = "/api/send/lms";

  private final GabiaProperty gabiaProperty;

  protected void sendSmsMessage(String receiver, String content) {
    String authToken = authenticate();

    RequestBody requestBody =
        new MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            // 수신번호가 두 개 이상인 경우 ',' 를 이용하여 입력 ex) 01011112222,01033334444
            .addFormDataPart("phone", receiver)
            .addFormDataPart("callback", gabiaProperty.sms().phone())
            .addFormDataPart("message", content)
            .addFormDataPart("refkey", generateReferenceKey())
            .build();

    Request request = buildPostRequest(URI_SEND_SMS, encodeAuthorization(authToken), requestBody);
    Response response = executeRequest(request);

    String message = extractSerializedDataIn("message", response);
    if (!message.trim().equals("Success")) {
      ClientError error = ClientError.GABIA_RESPONSE_UNAVAILABLE;
      error.addMessage(message);
      throw new ClientRequestException(error);
    }
  }

  protected void sendLmsMessage(String receiver, String title, String content) {
    String authToken = authenticate();

    RequestBody requestBody =
        new MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            // 수신번호가 두 개 이상인 경우 ',' 를 이용하여 입력 ex) 01011112222,01033334444
            .addFormDataPart("phone", receiver)
            .addFormDataPart("callback", gabiaProperty.sms().phone())
            .addFormDataPart("message", content)
            .addFormDataPart("refkey", generateReferenceKey())
            .addFormDataPart("subject", title)
            .build();

    Request request = buildPostRequest(URI_SEND_LMS, encodeAuthorization(authToken), requestBody);
    Response response = executeRequest(request);

    String message = extractSerializedDataIn("message", response);
    if (!message.trim().equals("Success")) {
      ClientError error = ClientError.GABIA_RESPONSE_UNAVAILABLE;
      error.addMessage(message);
      throw new ClientRequestException(error);
    }
  }

  private String generateReferenceKey() {
    return UUID.randomUUID().toString();
  }

  private String authenticate() {
    RequestBody requestBody =
        new MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("grant_type", "client_credentials")
            .build();

    Request request = buildPostRequest(URI_OAUTH_TOKEN, gabiaProperty.sms().key(), requestBody);
    Response response = executeRequest(request);
    return extractSerializedDataIn("access_token", response);
  }

  private String extractSerializedDataIn(String dataKey, Response response) {
    try {
      HashMap<String, String> result =
          new Gson().fromJson(Objects.requireNonNull(response.body()).string(), HashMap.class);
      String data = result.get(dataKey);
      if (data == null) {
        throw new ClientResponseException(ClientError.GABIA_RESPONSE_UNAVAILABLE);
      }
      return data;
    } catch (IOException | JsonSyntaxException | JsonIOException e) {
      throw new ClientResponseException(ClientError.GABIA_RESPONSE_BIND_FAIL);
    }
  }

  private Headers generateHeaderOfAuthorization(String authValue) {
    Map<String, String> headers = new HashMap<>();
    headers.put("Content-Type", "application/x-www-form-urlencoded");
    headers.put("Authorization", "Basic " + encodeAuthorization(authValue));
    headers.put("cache-control", "no-cache");
    return Headers.of(headers);
  }

  private String encodeAuthorization(String value) {
    return Base64.getEncoder()
        .encodeToString(
            String.format(FORMAT_AUTHORIZATION, gabiaProperty.sms().id(), value)
                .getBytes(StandardCharsets.UTF_8));
  }

  private Request buildPostRequest(
      String requestUri, String authorizationValue, RequestBody requestBody) {
    return new Request.Builder()
        .url(gabiaProperty.sms().url() + requestUri)
        .post(requestBody)
        .headers(generateHeaderOfAuthorization(authorizationValue))
        .build();
  }

  private Response executeRequest(Request request) {
    try {
      OkHttpClient client = new OkHttpClient();
      return client.newCall(request).execute();
    } catch (IOException e) {
      throw new ClientRequestException(ClientError.GABIA_REQUEST_INVALID_AUTH_DATA);
    }
  }
}
