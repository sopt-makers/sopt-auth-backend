package sopt.makers.authentication.adapter.out.external.exception.ClientException;

import sopt.makers.authentication.adapter.out.external.exception.ClientError;

public class PlaygroundRequestException extends ClientRequestException {
  public PlaygroundRequestException(ClientError failure) {
    super(failure);
  }
}
