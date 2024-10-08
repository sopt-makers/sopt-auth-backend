package sopt.makers.authentication.support.system.exception;

import sopt.makers.authentication.support.common.exception.BaseException;
import sopt.makers.authentication.support.system.code.failure.AuthFailure;

public class AuthException extends BaseException {
    public AuthException(AuthFailure failure, String message) {
        super(failure, message);
    }
}
