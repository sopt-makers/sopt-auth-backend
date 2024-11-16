package sopt.makers.authentication.support.exception.external;

import sopt.makers.authentication.support.code.external.failure.*;
import sopt.makers.authentication.support.exception.base.*;

public class AppleAuthException extends BaseException {
  public AppleAuthException(AppleError failure) {
    super(failure);
  }
}
