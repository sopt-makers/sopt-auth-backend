package sopt.makers.authentication.support.exception.external;

import sopt.makers.authentication.support.code.external.failure.ClientError;
import sopt.makers.authentication.support.exception.base.BaseException;

public class ClientResponseException extends BaseException {
  public ClientResponseException(ClientError failure) {
    super(failure);
  }
}
