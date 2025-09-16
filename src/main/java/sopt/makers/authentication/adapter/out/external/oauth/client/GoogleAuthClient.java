package sopt.makers.authentication.adapter.out.external.oauth.client;

import static sopt.makers.authentication.adapter.out.external.exception.ClientError.GOOGLE_REQUEST_FAIL;
import static sopt.makers.authentication.adapter.out.external.exception.ClientError.INVALID_GOOGLE_REQUEST_URL;
import static sopt.makers.authentication.adapter.out.external.oauth.OAuthConstant.GOOGLE_PUBLIC_KEY_SET_URL;

import sopt.makers.authentication.adapter.out.external.exception.ClientException.ClientRequestException;
import sopt.makers.authentication.adapter.out.external.exception.ClientException.ClientResponseException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;

import org.springframework.stereotype.Component;

import com.nimbusds.jose.jwk.JWKSet;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GoogleAuthClient {

  public JWKSet getPublicKeySet() {
    try {
      return JWKSet.load(new URI(GOOGLE_PUBLIC_KEY_SET_URL).toURL());
    } catch (URISyntaxException e) {
      throw new ClientRequestException(INVALID_GOOGLE_REQUEST_URL);
    } catch (IOException | ParseException e) {
      throw new ClientResponseException(GOOGLE_REQUEST_FAIL);
    }
  }
}
