package sopt.makers.authentication.application.user.api;

import static sopt.makers.authentication.support.constant.SystemConstant.API_KEY_HEADER;
import static sopt.makers.authentication.support.constant.SystemConstant.SERVICE_NAME_HEADER;

import sopt.makers.authentication.support.common.api.BaseResponse;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

public interface UserApi {
  ResponseEntity<BaseResponse<?>> getUserInformation(
      @RequestHeader(API_KEY_HEADER) String apiKey,
      @RequestHeader(SERVICE_NAME_HEADER) String serviceName,
      @RequestParam List<Long> userIds);
}
