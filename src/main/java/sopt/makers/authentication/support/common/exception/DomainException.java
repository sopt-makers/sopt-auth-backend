package sopt.makers.authentication.support.common.exception;

import sopt.makers.authentication.support.common.code.failure.DomainFailure;
import sopt.makers.authentication.support.common.exception.base.BaseException;

public class DomainException extends BaseException {
  public DomainException(final DomainFailure failure) {
    super(failure);
  }
}
