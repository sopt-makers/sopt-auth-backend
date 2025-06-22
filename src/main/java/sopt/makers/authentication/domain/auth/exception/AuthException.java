package sopt.makers.authentication.domain.auth.exception;

import sopt.makers.authentication.common.exception.BaseException;

public class AuthException extends BaseException {
  public AuthException(final AuthFailure failure) {
    super(failure);
  }
}
