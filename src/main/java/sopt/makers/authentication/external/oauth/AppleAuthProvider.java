package sopt.makers.authentication.external.oauth;

import static sopt.makers.authentication.support.code.external.failure.ClientError.APPLE_RESPONSE_UNAVAILABLE;
import static sopt.makers.authentication.support.code.external.failure.ClientError.FAIL_READ_APPLE_PRIVATE_KEY_FILE;
import static sopt.makers.authentication.support.code.external.failure.ClientError.INVALID_APPLE_AUTH_CODE;
import static sopt.makers.authentication.support.constant.OAuthConstant.ACCEPT;
import static sopt.makers.authentication.support.constant.OAuthConstant.ACCEPT_VALUE;
import static sopt.makers.authentication.support.constant.OAuthConstant.APPLE_ALGORITHM_HEADER;
import static sopt.makers.authentication.support.constant.OAuthConstant.APPLE_ALGORITHM_VALUE;
import static sopt.makers.authentication.support.constant.OAuthConstant.APPLE_KEY_ID_HEADER;
import static sopt.makers.authentication.support.constant.OAuthConstant.APPLE_TOKEN_URL;
import static sopt.makers.authentication.support.constant.OAuthConstant.CLIENT_ID;
import static sopt.makers.authentication.support.constant.OAuthConstant.CLIENT_SECRET;
import static sopt.makers.authentication.support.constant.OAuthConstant.CODE;
import static sopt.makers.authentication.support.constant.OAuthConstant.CONTENT_TYPE;
import static sopt.makers.authentication.support.constant.OAuthConstant.CONTENT_TYPE_VALUE;
import static sopt.makers.authentication.support.constant.OAuthConstant.GRANT_TYPE;
import static sopt.makers.authentication.support.constant.OAuthConstant.GRANT_TYPE_VALUE;

import sopt.makers.authentication.external.oauth.dto.IdTokenResponse;
import sopt.makers.authentication.support.exception.external.ClientRequestException;
import sopt.makers.authentication.support.exception.external.ClientResponseException;
import sopt.makers.authentication.support.value.AppleProperty;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
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
import okhttp3.ResponseBody;

@Component
@RequiredArgsConstructor
@Slf4j
public class AppleAuthProvider implements OAuthService {
  private static final Charset UTF_8 = StandardCharsets.UTF_8;
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
        .add(CLIENT_ID, clientId)
        .add(CLIENT_SECRET, clientSecret)
        .add(CODE, code)
        .add(GRANT_TYPE, GRANT_TYPE_VALUE)
        .build();
  }

  private String createClientSecret() {
    Date now = new Date();
    PrivateKey privateKey =
        getPrivateKey()
            .orElseThrow(() -> new ClientRequestException(FAIL_READ_APPLE_PRIVATE_KEY_FILE));

    return Jwts.builder()
        .setHeaderParam(APPLE_KEY_ID_HEADER, appleProperty.apple().key().id())
        .setHeaderParam(APPLE_ALGORITHM_HEADER, APPLE_ALGORITHM_VALUE)
        .setIssuedAt(now)
        .setExpiration(
            new Date(now.getTime() + appleProperty.apple().expiration().tokenExpiration()))
        .setIssuer(appleProperty.apple().team().id())
        .setAudience(appleProperty.apple().aud())
        .setSubject(appleProperty.apple().sub())
        .signWith(privateKey, SignatureAlgorithm.ES256)
        .compact();
  }

  private Optional<PrivateKey> getPrivateKey() {
    String appleKeyPath = appleProperty.apple().key().path();

    try (PEMParser pemParser = createPemParser(appleKeyPath)) {
      return parsePrivateKey(pemParser);
    } catch (IOException e) {
      log.error("Error while reading private key: {}", e.getMessage());
      return Optional.empty();
    }
  }

  private PEMParser createPemParser(String appleKeyPath) throws IOException {
    ClassPathResource resource = new ClassPathResource(appleKeyPath);
    String privateKey = new String(resource.getInputStream().readAllBytes(), UTF_8);
    return new PEMParser(new StringReader(privateKey));
  }

  private Optional<PrivateKey> parsePrivateKey(PEMParser pemParser) throws IOException {
    JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
    PrivateKeyInfo privateKeyInfo = (PrivateKeyInfo) pemParser.readObject();
    return Optional.of(converter.getPrivateKey(privateKeyInfo));
  }

  private static Request createHttpRequest(RequestBody requestBody) {
    return new Request.Builder()
        .url(APPLE_TOKEN_URL)
        .post(requestBody)
        .addHeader(CONTENT_TYPE, CONTENT_TYPE_VALUE)
        .addHeader(ACCEPT, ACCEPT_VALUE)
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
    ResponseBody responseBody = response.body();
    boolean isBodyNull = responseBody == null;

    if (isBodyNull) {
      throw new ClientResponseException(APPLE_RESPONSE_UNAVAILABLE);
    }
    return gson.fromJson(response.body().toString(), IdTokenResponse.class);
  }
}
