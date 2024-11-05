package sopt.makers.authentication.support.exception.base;

import sopt.makers.authentication.support.code.base.FailureCode;

public abstract class BaseException extends RuntimeException {

  private final FailureCode failure;

  public BaseException(final FailureCode failure) {
    super(failure.getMessage());
    this.failure = failure;
  }

  public FailureCode getError() {
    return this.failure;
  }
  ;
}
