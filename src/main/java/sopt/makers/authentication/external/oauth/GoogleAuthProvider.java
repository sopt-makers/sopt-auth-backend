package sopt.makers.authentication.external.oauth;

import static sopt.makers.authentication.support.constant.OAuthConstant.ACCEPT;
import static sopt.makers.authentication.support.constant.OAuthConstant.ACCEPT_VALUE;
import static sopt.makers.authentication.support.constant.OAuthConstant.CLIENT_ID;
import static sopt.makers.authentication.support.constant.OAuthConstant.CLIENT_SECRET;
import static sopt.makers.authentication.support.constant.OAuthConstant.CODE;
import static sopt.makers.authentication.support.constant.OAuthConstant.CONTENT_TYPE;
import static sopt.makers.authentication.support.constant.OAuthConstant.GOOGLE_TOKEN_URL;
import static sopt.makers.authentication.support.constant.OAuthConstant.GRANT_TYPE;
import static sopt.makers.authentication.support.constant.OAuthConstant.GRANT_TYPE_VALUE;
import static sopt.makers.authentication.support.constant.OAuthConstant.REDIRECT_URI;

import sopt.makers.authentication.external.oauth.dto.IdTokenResponse;
import sopt.makers.authentication.support.code.external.failure.ClientError;
import sopt.makers.authentication.support.exception.external.ClientRequestException;
import sopt.makers.authentication.support.exception.external.ClientResponseException;
import sopt.makers.authentication.support.value.GoogleProperty;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import lombok.RequiredArgsConstructor;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

@Component
@RequiredArgsConstructor
public class GoogleAuthProvider implements OAuthService {
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
        .add(CLIENT_ID, googleProperty.google().client().id())
        .add(CLIENT_SECRET, googleProperty.google().client().secret())
        .add(CODE, code)
        .add(GRANT_TYPE, GRANT_TYPE_VALUE)
        .add(REDIRECT_URI, googleProperty.google().redirect().url())
        .build();
  }

  private static Request createHttpRequest(FormBody formBody) {
    return new Request.Builder()
        .url(GOOGLE_TOKEN_URL)
        .post(formBody)
        .addHeader(CONTENT_TYPE, CONTENT_TYPE)
        .addHeader(ACCEPT, ACCEPT_VALUE)
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
    ResponseBody responseBody = response.body();
    boolean isBodyNull = responseBody == null;

    if (isBodyNull) {
      throw new ClientResponseException(ClientError.GOOGLE_RESPONSE_UNAVAILABLE);
    }
    return gson.fromJson(responseBody.toString(), IdTokenResponse.class);
  }
}
