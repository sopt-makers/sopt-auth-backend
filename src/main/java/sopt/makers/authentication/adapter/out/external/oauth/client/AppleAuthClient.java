package sopt.makers.authentication.adapter.out.external.oauth.client;

import static sopt.makers.authentication.adapter.out.external.exception.ClientError.APPLE_REQUEST_FAIL;
import static sopt.makers.authentication.adapter.out.external.exception.ClientError.INVALID_APPLE_REQUEST_URL;
import static sopt.makers.authentication.adapter.out.external.oauth.OAuthConstant.APPLE_PUBLIC_KEY_SET_URL;

import sopt.makers.authentication.adapter.out.external.exception.ClientRequestException;
import sopt.makers.authentication.adapter.out.external.exception.ClientResponseException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;

import org.springframework.stereotype.Component;

import com.nimbusds.jose.jwk.JWKSet;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AppleAuthClient {

  public JWKSet getPublicKeySet() {
    try {
      return JWKSet.load(new URI(APPLE_PUBLIC_KEY_SET_URL).toURL());
    } catch (URISyntaxException e) {
      throw new ClientRequestException(INVALID_APPLE_REQUEST_URL);
    } catch (IOException | ParseException e) {
      throw new ClientResponseException(APPLE_REQUEST_FAIL);
    }
  }
}
