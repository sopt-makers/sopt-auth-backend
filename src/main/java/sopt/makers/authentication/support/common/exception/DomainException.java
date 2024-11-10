package sopt.makers.authentication.support.common.exception;

import sopt.makers.authentication.support.common.code.failure.*;
import sopt.makers.authentication.support.common.exception.base.*;

public class DomainException extends BaseException {
  public DomainException(final AuthFailure failure) {
    super(failure);
  }
}
