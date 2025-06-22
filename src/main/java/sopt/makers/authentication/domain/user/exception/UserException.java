package sopt.makers.authentication.domain.user.exception;

import sopt.makers.authentication.common.exception.BaseException;

public class UserException extends BaseException {
  public UserException(UserFailure failure) {
    super(failure);
  }
}
