package sopt.makers.authentication.support.util.code.failure;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import sopt.makers.authentication.support.common.code.Failure;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum TokenFailure implements Failure {

    ;
    private final HttpStatus status;
    private final String message;
}
