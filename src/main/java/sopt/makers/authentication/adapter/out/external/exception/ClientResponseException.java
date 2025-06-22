package sopt.makers.authentication.adapter.out.external.exception;

import sopt.makers.authentication.common.exception.BaseException;

public class ClientResponseException extends BaseException {
  public ClientResponseException(ClientError failure) {
    super(failure);
  }
}
