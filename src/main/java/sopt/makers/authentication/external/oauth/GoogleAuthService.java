package sopt.makers.authentication.external.oauth;

import sopt.makers.authentication.external.oauth.dto.IdTokenResponse;
import sopt.makers.authentication.support.code.external.failure.ClientError;
import sopt.makers.authentication.support.exception.external.ClientRequestException;
import sopt.makers.authentication.support.exception.external.ClientResponseException;
import sopt.makers.authentication.support.value.GoogleProperty;

import java.io.IOException;

import com.google.gson.Gson;

import lombok.RequiredArgsConstructor;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@RequiredArgsConstructor
public class GoogleAuthService implements OAuthService {
  private static final String GRANT_TYPE = "authorization_code";
  private static final String HOST = "https://oauth2.googleapis.com/token";
  private final GoogleProperty googleProperty;
  private final Gson gson;
  private final OkHttpClient client;

  @Override
  public IdTokenResponse getIdTokenByCode(String code) {
    FormBody formBody = createTokenRequestFormBody(code);
    Request request = createHttpRequest(formBody);
    Response response = executeRequest(request);

    return parseResponseBody(response);
  }

  private FormBody createTokenRequestFormBody(String code) {
    return new FormBody.Builder()
        .add("client_id", googleProperty.google().client().id())
        .add("client_secret", googleProperty.google().client().secret())
        .add("code", code)
        .add("grant_type", GRANT_TYPE)
        .add("redirect_uri", googleProperty.google().redirect().url())
        .build();
  }

  private static Request createHttpRequest(FormBody formBody) {
    return new Request.Builder()
        .url(HOST)
        .post(formBody)
        .addHeader("Content-Type", "application/x-www-form-urlencoded")
        .addHeader("Accept", "application/json")
        .build();
  }

  private Response executeRequest(Request request) {
    try {
      Response response = client.newCall(request).execute();

      validateResponse(response);
      return response;
    } catch (IOException e) {
      throw new ClientResponseException(ClientError.GOOGLE_RESPONSE_UNAVAILABLE);
    }
  }

  private void validateResponse(Response response) {
    boolean isNotSuccessResponse = !response.isSuccessful();

    if (isNotSuccessResponse) {
      throw new ClientRequestException(ClientError.INVALID_GOOGLE_AUTH_CODE);
    }
  }

  private IdTokenResponse parseResponseBody(Response response) {
    boolean containsResponseBody = response.body() != null;

    if (containsResponseBody) {
      String responseBody = response.body().toString();
      return gson.fromJson(responseBody, IdTokenResponse.class);
    }
    throw new ClientResponseException(ClientError.GOOGLE_RESPONSE_UNAVAILABLE);
  }
}
