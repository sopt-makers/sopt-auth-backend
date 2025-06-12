package sopt.makers.authentication.support.util;

import java.util.Map;

import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.RequestBody;

public final class RequestUtil {

  public static Request createGetRequest(String url, Headers headers) {
    return new Request.Builder().url(url).headers(headers).get().build();
  }

  public static Request createPostRequest(String url, Headers headers, RequestBody body) {
    return new Request.Builder().url(url).headers(headers).post(body).build();
  }

  public static Headers createHeaders(Map<String, String> values) {
    return Headers.of(values);
  }
}
