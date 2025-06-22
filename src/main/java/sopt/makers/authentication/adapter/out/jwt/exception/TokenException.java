package sopt.makers.authentication.adapter.out.jwt.exception;

import sopt.makers.authentication.common.exception.BaseException;

public class TokenException extends BaseException {

  public TokenException(final TokenFailure failure) {
    super(failure);
  }
}
