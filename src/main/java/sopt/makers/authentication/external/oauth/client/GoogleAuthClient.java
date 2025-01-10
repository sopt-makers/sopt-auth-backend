package sopt.makers.authentication.external.oauth.client;

import static sopt.makers.authentication.support.code.external.failure.ClientError.*;
import static sopt.makers.authentication.support.constant.OAuthConstant.*;

import sopt.makers.authentication.external.oauth.dto.IdTokenResponse;
import sopt.makers.authentication.support.exception.external.ClientRequestException;
import sopt.makers.authentication.support.exception.external.ClientResponseException;
import sopt.makers.authentication.support.util.RequestUtil;
import sopt.makers.authentication.support.value.GoogleOAuthProperty;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.nimbusds.jose.jwk.JWKSet;

import lombok.RequiredArgsConstructor;
import okhttp3.*;

@Component
@RequiredArgsConstructor
public class GoogleAuthClient {
  private final GoogleOAuthProperty googleOAuthProperty;
  private final Gson gson;
  private final OkHttpClient client;

  public IdTokenResponse getIdToken(final String secret, final String code) {
    Headers header = createTokenRequestHeader();
    FormBody formBody = createTokenRequestFormBody(secret, code);

    Request tokenRequest = RequestUtil.createPostRequest(GOOGLE_TOKEN_URL, header, formBody);
    Response tokenResponse = executeRequest(tokenRequest);

    return parseResponseBody(tokenResponse, IdTokenResponse.class);
  }

  public JWKSet getPublicKeySet() {
    try {
      return JWKSet.load(new URI(GOOGLE_PUBLIC_KEY_SET_URL).toURL());
    } catch (URISyntaxException e) {
      throw new ClientRequestException(INVALID_APPLE_REQUEST_URL);
    } catch (IOException | ParseException e) {
      throw new ClientResponseException(APPLE_REQUEST_FAIL);
    }
  }

  private FormBody createTokenRequestFormBody(String secret, String code) {
    return new FormBody.Builder()
        .add(CLIENT_ID, googleOAuthProperty.client().id())
        .add(CLIENT_SECRET, secret)
        .add(CODE, code)
        .add(GRANT_TYPE, GRANT_TYPE_VALUE)
        .add(REDIRECT_URI, googleOAuthProperty.redirect().url())
        .build();
  }

  private Headers createTokenRequestHeader() {
    Map<String, String> headers = new HashMap<>();
    headers.put(CONTENT_TYPE, CONTENT_TYPE_VALUE);
    headers.put(ACCEPT, ACCEPT_VALUE);
    return RequestUtil.createHeaders(headers);
  }

  private Response executeRequest(Request request) {
    try {
      Response response = client.newCall(request).execute();

      validateResponse(response);
      return response;
    } catch (IOException e) {
      throw new ClientResponseException(GOOGLE_RESPONSE_UNAVAILABLE);
    }
  }

  private void validateResponse(Response response) {
    boolean isNotSuccessResponse = !response.isSuccessful();

    if (isNotSuccessResponse) {
      throw new ClientRequestException(GOOGLE_REQUEST_FAIL);
    }
  }

  private <T> T parseResponseBody(Response response, Class<T> classOfType) {
    ResponseBody responseBody = response.body();
    boolean isBodyNull = responseBody == null;

    if (isBodyNull) {
      throw new ClientResponseException(APPLE_RESPONSE_UNAVAILABLE);
    }
    return gson.fromJson(response.body().toString(), classOfType);
  }
}
