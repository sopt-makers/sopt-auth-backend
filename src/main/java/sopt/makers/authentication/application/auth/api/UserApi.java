package sopt.makers.authentication.application.auth.api;

import sopt.makers.authentication.support.common.api.BaseResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

public interface UserApi {
  ResponseEntity<BaseResponse<?>> getUserInformation(@PathVariable long userId);
}
