package sopt.makers.authentication.common.code;

import org.springframework.http.HttpStatus;

public interface BaseCode {
  HttpStatus getStatus();

  String getMessage();
}
