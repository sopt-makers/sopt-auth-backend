package sopt.makers.authentication.support.exception.support;

import sopt.makers.authentication.support.code.support.failure.TokenFailure;
import sopt.makers.authentication.support.exception.base.BaseException;

public class TokenException extends BaseException {

  public TokenException(final TokenFailure failure) {
    super(failure);
  }
}
