package sopt.makers.authentication.support.code.base;

import org.springframework.http.HttpStatus;

public interface BaseCode {
  HttpStatus getStatus();

  String getMessage();
}
