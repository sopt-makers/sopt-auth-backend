package sopt.makers.authentication.support.system.code.failure;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import sopt.makers.authentication.support.common.code.Failure;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum AuthFailure implements Failure {

    // 401

    // 403


    ;
    private final HttpStatus status;
    private final String message;

}
