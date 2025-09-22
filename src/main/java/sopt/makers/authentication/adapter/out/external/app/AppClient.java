package sopt.makers.authentication.adapter.out.external.app;

import static sopt.makers.authentication.adapter.out.external.exception.ClientError.APP_REQUEST_FAIL;
import static sopt.makers.authentication.adapter.out.external.exception.ClientError.APP_RESPONSE_UNAVAILABLE;

import sopt.makers.authentication.adapter.out.external.exception.ClientException.AppRequestException;
import sopt.makers.authentication.adapter.out.external.exception.ClientException.AppResponseException;

import java.io.IOException;
import java.util.Map;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Component
@RequiredArgsConstructor
@Slf4j
public class AppClient {
  private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
  private static final String HEADER_API_KEY = "apiKey";
  private static final String HEADER_CONTENT_TYPE = "Content-Type";
  private static final String CONTENT_TYPE_JSON = "application/json";
  private static final String ENDPOINT_CREATE_PROFILE = "internal/api/v1/members";
  private static final String ENDPOINT_DELETE_PROFILE = "internal/api/v1/members";
  private static final String FIELD_USER_ID = "userId";
  private final AppProperty appProperty;
  private final OkHttpClient client;
  private final Gson gson;

  @Retryable(
      value = {AppRequestException.class, AppResponseException.class},
      maxAttempts = 3,
      backoff = @Backoff(delay = 2000, multiplier = 1.5))
  public void createMemberProfile(Long memberId) {
    String requestBody = gson.toJson(Map.of(FIELD_USER_ID, memberId));
    RequestBody body = RequestBody.create(requestBody, JSON);
    HttpUrl url =
        HttpUrl.parse(appProperty.url())
            .newBuilder()
            .addPathSegments(ENDPOINT_CREATE_PROFILE)
            .build();
    Request httpRequest =
        new Request.Builder()
            .url(url)
            .addHeader(HEADER_API_KEY, appProperty.key())
            .addHeader(HEADER_CONTENT_TYPE, CONTENT_TYPE_JSON)
            .post(body)
            .build();

    try (Response response = client.newCall(httpRequest).execute()) {
      if (response.code() != 200) {
        log.error("앱 유저 생성 요청 실패: code={}, message={}", response.code(), response.message());
        throw new AppRequestException(APP_REQUEST_FAIL);
      }
    } catch (IOException e) {
      throw new AppResponseException(APP_RESPONSE_UNAVAILABLE);
    }
  }

  @Retryable(
      value = {AppRequestException.class, AppResponseException.class},
      maxAttempts = 3,
      backoff = @Backoff(delay = 2000, multiplier = 1.5))
  public void deleteMemberProfile(Long memberId) {
    okhttp3.HttpUrl url =
        okhttp3.HttpUrl.parse(appProperty.url())
            .newBuilder()
            .addPathSegments(ENDPOINT_DELETE_PROFILE)
            .addPathSegment(String.valueOf(memberId))
            .build();

    Request httpRequest =
        new Request.Builder()
            .url(url)
            .addHeader(HEADER_API_KEY, appProperty.key())
            .addHeader(HEADER_CONTENT_TYPE, CONTENT_TYPE_JSON)
            .delete()
            .build();

    try (Response response = client.newCall(httpRequest).execute()) {
      if (response.code() != 200) {
        log.error("앱 유저 삭제 요청 실패: code={}, message={}", response.code(), response.message());
        throw new AppRequestException(APP_REQUEST_FAIL);
      }
    } catch (IOException e) {
      throw new AppResponseException(APP_RESPONSE_UNAVAILABLE);
    }
  }
}
