package sopt.makers.authentication.adapter.out.external.playground;

import static sopt.makers.authentication.adapter.out.external.exception.ClientError.PLAYGROUND_REQUEST_FAIL;
import static sopt.makers.authentication.adapter.out.external.exception.ClientError.PLAYGROUND_RESPONSE_UNAVAILABLE;

import sopt.makers.authentication.adapter.out.external.exception.ClientRequestException;
import sopt.makers.authentication.adapter.out.external.exception.ClientResponseException;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import lombok.RequiredArgsConstructor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Component
@RequiredArgsConstructor
public class PlaygroundClient {
  private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
  private static final String HEADER_API_KEY = "apiKey";
  private static final String HEADER_CONTENT_TYPE = "Content-Type";
  private static final String CONTENT_TYPE_JSON = "application/json";
  private static final String ENDPOINT_CREATE_PROFILE = "internal/api/v1/members/profile";
  private static final String FIELD_USER_ID = "userId";
  private final PlaygroundProperty playgroundProperty;
  private final OkHttpClient client;
  private final Gson gson;

  public void createMemberProfile(Long memberId) {
    String requestBody = gson.toJson(Map.of(FIELD_USER_ID, memberId));
    RequestBody body = RequestBody.create(requestBody, JSON);
    okhttp3.HttpUrl url =
        okhttp3.HttpUrl.parse(playgroundProperty.url())
            .newBuilder()
            .addPathSegments(ENDPOINT_CREATE_PROFILE)
            .build();
    Request httpRequest =
        new Request.Builder()
            .url(url)
            .addHeader(HEADER_API_KEY, playgroundProperty.key())
            .addHeader(HEADER_CONTENT_TYPE, CONTENT_TYPE_JSON)
            .post(body)
            .build();

    try (Response response = client.newCall(httpRequest).execute()) {
      if (response.code() != 201) {
        throw new ClientRequestException(PLAYGROUND_REQUEST_FAIL);
      }
    } catch (IOException e) {
      throw new ClientResponseException(PLAYGROUND_RESPONSE_UNAVAILABLE);
    }
  }
}
