package sopt.makers.authentication.adapter.out.external.exception.ClientException;

import sopt.makers.authentication.adapter.out.external.exception.ClientError;

public class PlaygroundResponseException extends ClientResponseException {
  public PlaygroundResponseException(ClientError failure) {
    super(failure);
  }
}
