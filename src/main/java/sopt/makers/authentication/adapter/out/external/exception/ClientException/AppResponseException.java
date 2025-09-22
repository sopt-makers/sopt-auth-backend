package sopt.makers.authentication.adapter.out.external.exception.ClientException;

import sopt.makers.authentication.adapter.out.external.exception.ClientError;

public class AppResponseException extends ClientResponseException {
  public AppResponseException(ClientError failure) {
    super(failure);
  }
}
