package sopt.makers.authentication.support.exception.domain;

import sopt.makers.authentication.support.code.domain.failure.AuthFailure;
import sopt.makers.authentication.support.exception.base.BaseException;

public class AuthException extends BaseException {
  public AuthException(final AuthFailure failure) {
    super(failure);
  }
}
