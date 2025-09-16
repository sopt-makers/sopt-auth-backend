package sopt.makers.authentication.adapter.out.external.exception.ClientException;

import sopt.makers.authentication.adapter.out.external.exception.ClientError;

public class AppRequestException extends ClientRequestException {
  public AppRequestException(ClientError failure) {
    super(failure);
  }
}
