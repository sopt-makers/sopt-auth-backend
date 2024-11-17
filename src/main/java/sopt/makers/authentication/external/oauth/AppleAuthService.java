package sopt.makers.authentication.external.oauth;

import static sopt.makers.authentication.support.code.external.failure.ClientError.APPLE_RESPONSE_UNAVAILABLE;
import static sopt.makers.authentication.support.code.external.failure.ClientError.FAIL_READ_APPLE_PRIVATE_KEY_FILE;
import static sopt.makers.authentication.support.code.external.failure.ClientError.INVALID_APPLE_AUTH_CODE;
import static sopt.makers.authentication.support.constant.OAuthConstant.ACCEPT;
import static sopt.makers.authentication.support.constant.OAuthConstant.APPLE_TOKEN_EXPIRATION_TIME;
import static sopt.makers.authentication.support.constant.OAuthConstant.APPLE_TOKEN_URL;
import static sopt.makers.authentication.support.constant.OAuthConstant.CONTENT_TYPE;
import static sopt.makers.authentication.support.constant.OAuthConstant.GRANT_TYPE;

import sopt.makers.authentication.external.oauth.dto.IdTokenResponse;
import sopt.makers.authentication.support.exception.external.ClientRequestException;
import sopt.makers.authentication.support.exception.external.ClientResponseException;
import sopt.makers.authentication.support.value.AppleProperty;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.util.Date;
import java.util.Optional;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Component
@RequiredArgsConstructor
@Slf4j
public class AppleAuthService implements OAuthService {
  private final AppleProperty appleProperty;
  private final Gson gson;
  private final OkHttpClient client;

  @Override
  public IdTokenResponse getIdTokenByCode(final String code) {
    FormBody formBody = createTokenRequestFormBody(code);
    Request request = createHttpRequest(formBody);
    Response response = executeRequest(request);

    return parseResponseBody(response);
  }

  private FormBody createTokenRequestFormBody(final String code) {
    String clientId = appleProperty.apple().sub();
    String clientSecret = createClientSecret();
    return new FormBody.Builder()
        .add("client_id", clientId)
        .add("client_secret", clientSecret)
        .add("code", code)
        .add("grant_type", GRANT_TYPE)
        .build();
  }

  private String createClientSecret() {
    Date now = new Date();
    PrivateKey privateKey =
        getPrivateKey()
            .orElseThrow(() -> new ClientRequestException(FAIL_READ_APPLE_PRIVATE_KEY_FILE));

    return Jwts.builder()
        .setHeaderParam("kid", appleProperty.apple().key().id())
        .setHeaderParam("alg", "ES256")
        .setIssuedAt(now)
        .setExpiration(new Date(now.getTime() + APPLE_TOKEN_EXPIRATION_TIME))
        .setIssuer(appleProperty.apple().team().id())
        .setAudience(appleProperty.apple().aud())
        .setSubject(appleProperty.apple().sub())
        .signWith(privateKey, SignatureAlgorithm.ES256)
        .compact();
  }

  private Optional<PrivateKey> getPrivateKey() {
    String appleKeyPath = appleProperty.apple().key().path();

    try {
      ClassPathResource resource = new ClassPathResource(appleKeyPath);
      String privateKey =
          new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
      StringReader pemReader = new StringReader(privateKey);
      PEMParser pemParser = new PEMParser(pemReader);
      JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
      PrivateKeyInfo privateKeyInfo = (PrivateKeyInfo) pemParser.readObject();

      return Optional.of(converter.getPrivateKey(privateKeyInfo));
    } catch (IOException e) {
      log.error(e.getMessage());
      return Optional.empty();
    }
  }

  private static Request createHttpRequest(RequestBody requestBody) {
    return new Request.Builder()
        .url(APPLE_TOKEN_URL)
        .post(requestBody)
        .addHeader("Content-Type", CONTENT_TYPE)
        .addHeader("Accept", ACCEPT)
        .build();
  }

  private Response executeRequest(Request request) {
    try {
      Response response = client.newCall(request).execute();

      validateResponse(response);
      return response;
    } catch (IOException e) {
      throw new ClientResponseException(APPLE_RESPONSE_UNAVAILABLE);
    }
  }

  private void validateResponse(Response response) {
    boolean isNotSuccessResponse = !response.isSuccessful();

    if (isNotSuccessResponse) {
      throw new ClientRequestException(INVALID_APPLE_AUTH_CODE);
    }
  }

  private IdTokenResponse parseResponseBody(Response response) {
    boolean containsResponseBody = response.body() != null;

    if (containsResponseBody) {
      String responseBody = response.body().toString();
      return gson.fromJson(responseBody, IdTokenResponse.class);
    }
    throw new ClientResponseException(APPLE_RESPONSE_UNAVAILABLE);
  }
}
