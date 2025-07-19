package sopt.makers.authentication.adapter.out.common.exception;

import sopt.makers.authentication.common.code.FailureCode;
import sopt.makers.authentication.common.exception.BaseException;

public class CacheException extends BaseException {
  public CacheException(FailureCode failure) {
    super(failure);
  }
}
