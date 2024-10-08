package sopt.makers.authentication.support.util.exception;

import sopt.makers.authentication.support.common.code.Failure;
import sopt.makers.authentication.support.common.exception.BaseException;

public class TokenException extends BaseException {
    public TokenException(Failure failure) {
        super(failure);
    }
}
