package sopt.makers.authentication.support.exception.external;

import sopt.makers.authentication.support.code.external.failure.ClientError;
import sopt.makers.authentication.support.exception.base.BaseException;

public class ClientRequestException extends BaseException {
  public ClientRequestException(ClientError failure) {
    super(failure);
  }
}
