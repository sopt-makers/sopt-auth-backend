package sopt.makers.authentication.support.common.exception;

import sopt.makers.authentication.support.common.code.failure.AuthFailure;
import sopt.makers.authentication.support.common.exception.base.BaseException;

public class AuthException extends BaseException {
  public AuthException(final AuthFailure failure) {
    super(failure);
  }
}
