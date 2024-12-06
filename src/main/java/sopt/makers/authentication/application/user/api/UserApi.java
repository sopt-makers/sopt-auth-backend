package sopt.makers.authentication.application.user.api;

import sopt.makers.authentication.support.common.api.BaseResponse;

import org.springframework.http.ResponseEntity;

public interface UserApi {

  ResponseEntity<BaseResponse<?>> getRegisterSocialAccountPlatform(String name, String phone);
}
