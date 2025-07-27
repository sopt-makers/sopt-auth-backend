package sopt.makers.authentication.adapter.in.web.controller.user;

import static sopt.makers.authentication.common.constant.PagingConstant.DEFAULT_LIMIT;
import static sopt.makers.authentication.common.constant.PagingConstant.DEFAULT_OFFSET;
import static sopt.makers.authentication.common.constant.PagingConstant.DEFAULT_ORDER_BY;
import static sopt.makers.authentication.common.constant.PagingConstant.MAX_LIMIT;
import static sopt.makers.authentication.common.constant.SystemConstant.API_KEY_HEADER;
import static sopt.makers.authentication.common.constant.SystemConstant.SERVICE_NAME_HEADER;

import sopt.makers.authentication.adapter.in.web.common.BaseResponse;
import sopt.makers.authentication.adapter.in.web.dto.user.request.UserRequest;
import sopt.makers.authentication.domain.user.Part;
import sopt.makers.authentication.domain.user.Team;
import sopt.makers.authentication.domain.user.UserOrderBy;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;

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

  ResponseEntity<BaseResponse<?>> getUserProfileByFilters(
      @RequestHeader(API_KEY_HEADER) String apiKey,
      @RequestHeader(SERVICE_NAME_HEADER) String serviceName,
      @RequestParam(required = false) Integer generation,
      @RequestParam(required = false) Part part,
      @RequestParam(required = false) String name,
      @RequestParam(required = false) Team team,
      @RequestParam(defaultValue = DEFAULT_OFFSET) int offset,
      @RequestParam(defaultValue = DEFAULT_LIMIT) @Positive @Max(MAX_LIMIT) int limit,
      @RequestParam(defaultValue = DEFAULT_ORDER_BY) UserOrderBy orderBy);

  ResponseEntity<BaseResponse<?>> getUserCountByGeneration(
      @RequestHeader(API_KEY_HEADER) String apiKey,
      @RequestHeader(SERVICE_NAME_HEADER) String serviceName,
      @RequestParam int generation);
}
