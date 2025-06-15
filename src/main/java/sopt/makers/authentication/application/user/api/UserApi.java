package sopt.makers.authentication.application.user.api;

import static sopt.makers.authentication.support.constant.SystemConstant.API_KEY_HEADER;
import static sopt.makers.authentication.support.constant.SystemConstant.SERVICE_NAME_HEADER;

import sopt.makers.authentication.application.user.dto.request.UserRequest;
import sopt.makers.authentication.domain.user.Part;
import sopt.makers.authentication.support.common.api.BaseResponse;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

public interface UserApi {
  ResponseEntity<BaseResponse<?>> getUserProfile(
      @RequestHeader(API_KEY_HEADER) String apiKey,
      @RequestHeader(SERVICE_NAME_HEADER) String serviceName,
      @RequestParam List<Long> userIds);

  ResponseEntity<BaseResponse<?>> updateUserProfile(
      @RequestHeader(API_KEY_HEADER) String apiKey,
      @RequestHeader(SERVICE_NAME_HEADER) String serviceName,
      @PathVariable Long userId,
      @Valid @RequestBody UserRequest.UserProfileInfo userProfileInfo);

  ResponseEntity<BaseResponse<?>> getUserProfileByActivity(
      @RequestHeader(API_KEY_HEADER) String apiKey,
      @RequestHeader(SERVICE_NAME_HEADER) String serviceName,
      @RequestParam(required = false) Integer generation,
      @RequestParam(required = false) Part part);
}
