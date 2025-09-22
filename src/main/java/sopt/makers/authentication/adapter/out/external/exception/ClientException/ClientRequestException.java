package sopt.makers.authentication.adapter.out.external.exception.ClientException;

import sopt.makers.authentication.adapter.out.external.exception.ClientError;
import sopt.makers.authentication.common.exception.BaseException;

public class ClientRequestException extends BaseException {
  public ClientRequestException(ClientError failure) {
    super(failure);
  }
}
