package sopt.makers.authentication.support.exception.support;

import sopt.makers.authentication.support.code.support.failure.CommonFailure;
import sopt.makers.authentication.support.exception.base.BaseException;

public class FilterException extends BaseException {

  public FilterException(final CommonFailure failure) {
    super(failure);
  }
}
