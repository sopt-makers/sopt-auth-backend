package sopt.makers.authentication.support.exception.support;

import sopt.makers.authentication.support.code.support.failure.ResourceFailure;
import sopt.makers.authentication.support.exception.base.BaseException;

public class ResourceException extends BaseException {

  public ResourceException(final ResourceFailure failure) {
    super(failure);
  }
}
