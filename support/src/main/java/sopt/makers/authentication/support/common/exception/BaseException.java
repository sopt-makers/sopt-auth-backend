package sopt.makers.authentication.support.common.exception;

import lombok.Getter;
import sopt.makers.authentication.support.common.code.Failure;

@Getter
public class BaseException extends RuntimeException{

    private final Failure failure;
    public BaseException(Failure failure) {
        super(failure.getMessage());
        this.failure = failure;
    }
}
