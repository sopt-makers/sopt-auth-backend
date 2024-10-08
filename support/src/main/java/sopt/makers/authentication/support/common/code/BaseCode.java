package sopt.makers.authentication.support.common.code;

import org.springframework.http.HttpStatus;

public interface BaseCode {
    HttpStatus getStatus();

    String getMessage();
}
