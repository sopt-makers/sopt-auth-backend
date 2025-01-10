package sopt.makers.authentication.external.oauth.client;

import static sopt.makers.authentication.support.code.external.failure.ClientError.*;
import static sopt.makers.authentication.support.constant.OAuthConstant.*;

import sopt.makers.authentication.support.exception.external.ClientRequestException;
import sopt.makers.authentication.support.exception.external.ClientResponseException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;

import org.springframework.stereotype.Component;

import com.nimbusds.jose.jwk.JWKSet;

import lombok.RequiredArgsConstructor;
import okhttp3.*;

@Component
@RequiredArgsConstructor
public class GoogleAuthClient {

  public JWKSet getPublicKeySet() {
    try {
      return JWKSet.load(new URI(GOOGLE_PUBLIC_KEY_SET_URL).toURL());
    } catch (URISyntaxException e) {
      throw new ClientRequestException(INVALID_APPLE_REQUEST_URL);
    } catch (IOException | ParseException e) {
      throw new ClientResponseException(APPLE_REQUEST_FAIL);
    }
  }
}
