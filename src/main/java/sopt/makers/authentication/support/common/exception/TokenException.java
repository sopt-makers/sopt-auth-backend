package sopt.makers.authentication.support.common.exception;

import sopt.makers.authentication.support.common.code.failure.TokenFailure;
import sopt.makers.authentication.support.common.exception.base.BaseException;

public class TokenException extends BaseException {

  public TokenException(final TokenFailure failure) {
    super(failure);
  }
}
