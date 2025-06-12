package sopt.makers.authentication.support.exception.domain;

import sopt.makers.authentication.support.code.domain.failure.UserFailure;
import sopt.makers.authentication.support.exception.base.BaseException;

public class UserException extends BaseException {
  public UserException(UserFailure failure) {
    super(failure);
  }
}
