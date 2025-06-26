package sopt.makers.authentication.adapter.out.jwt.exception;

import sopt.makers.authentication.common.exception.BaseException;

public class ResourceException extends BaseException {

  public ResourceException(final ResourceFailure failure) {
    super(failure);
  }
}
