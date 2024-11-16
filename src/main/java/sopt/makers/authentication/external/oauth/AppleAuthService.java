package sopt.makers.authentication.external.oauth;

import static sopt.makers.authentication.support.code.external.failure.AppleError.APPLE_INTERNAL_SERVER_ERROR;
import static sopt.makers.authentication.support.code.external.failure.AppleError.FAIL_READ_APPLE_PRIVATE_KEY_FILE;
import static sopt.makers.authentication.support.code.external.failure.AppleError.INVALID_APPLE_AUTH_CODE;

import sopt.makers.authentication.external.oauth.dto.IdTokenResponse;
import sopt.makers.authentication.support.code.external.failure.ClientError;
import sopt.makers.authentication.support.exception.external.AppleAuthException;
import sopt.makers.authentication.support.exception.external.ClientResponseException;
import sopt.makers.authentication.support.value.AppleProperty;

import java.io.IOException;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.util.Date;
import java.util.Optional;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMException;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;

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

@RequiredArgsConstructor
@Slf4j
public class AppleAuthService implements OAuthService {
  private static final int TOKEN_EXPIRATION_TIME = 3600 * 1000; // 1 hour
  private static final String GRANT_TYPE = "authorization_code";
  private static final String TOKEN_URL = "https://appleid.apple.com/auth/token";
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
        getPrivateKey().orElseThrow(() -> new AppleAuthException(FAIL_READ_APPLE_PRIVATE_KEY_FILE));

    return Jwts.builder()
        .setHeaderParam("kid", appleProperty.apple().key().id())
        .setHeaderParam("alg", "ES256")
        .setIssuedAt(now)
        .setExpiration(new Date(now.getTime() + TOKEN_EXPIRATION_TIME))
        .setIssuer(appleProperty.apple().team().id())
        .setAudience(appleProperty.apple().aud())
        .setSubject(appleProperty.apple().sub())
        .signWith(privateKey, SignatureAlgorithm.ES256)
        .compact();
  }

  private Optional<PrivateKey> getPrivateKey() {
    String appleKeyPath = appleProperty.apple().key().path();
    return readKeyFileContent(appleKeyPath)
        .flatMap(this::parsePrivateKeyInfo)
        .flatMap(this::convertToPrivateKey);
  }

  private Optional<String> readKeyFileContent(String path) {
    try {
      String content =
          Files.readString(
              Paths.get(getClass().getClassLoader().getResource(path).toURI()),
              StandardCharsets.UTF_8);
      return Optional.of(content);
    } catch (IOException | URISyntaxException e) {
      log.error(e.getMessage());
      return Optional.empty();
    }
  }

  private Optional<PrivateKeyInfo> parsePrivateKeyInfo(String privateKeyContent) {
    try (PEMParser pemParser = new PEMParser(new StringReader(privateKeyContent))) {
      return Optional.ofNullable((PrivateKeyInfo) pemParser.readObject());
    } catch (IOException e) {
      log.error(e.getMessage());
      return Optional.empty();
    }
  }

  private Optional<PrivateKey> convertToPrivateKey(PrivateKeyInfo privateKeyInfo) {
    try {
      PrivateKey privateKey = new JcaPEMKeyConverter().getPrivateKey(privateKeyInfo);
      return Optional.ofNullable(privateKey);
    } catch (PEMException e) {
      log.error(e.getMessage());
      return Optional.empty();
    }
  }

  private static Request createHttpRequest(RequestBody requestBody) {
    return new Request.Builder()
        .url(TOKEN_URL)
        .post(requestBody)
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
      throw new AppleAuthException(APPLE_INTERNAL_SERVER_ERROR);
    }
  }

  private void validateResponse(Response response) {
    boolean isNotSuccessResponse = !response.isSuccessful();

    if (isNotSuccessResponse) {
      throw new AppleAuthException(INVALID_APPLE_AUTH_CODE);
    }
  }

  private IdTokenResponse parseResponseBody(Response response) {
    boolean containsResponseBody = response.body() != null;

    if (containsResponseBody) {
      String responseBody = response.body().toString();
      return gson.fromJson(responseBody, IdTokenResponse.class);
    }
    throw new ClientResponseException(ClientError.APPLE_RESPONSE_UNAVAILABLE);
  }
}
