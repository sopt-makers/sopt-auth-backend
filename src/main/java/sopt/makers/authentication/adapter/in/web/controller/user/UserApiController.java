package sopt.makers.authentication.adapter.in.web.controller.user;

import static sopt.makers.authentication.common.constant.SystemConstant.API_KEY_HEADER;
import static sopt.makers.authentication.common.constant.SystemConstant.SERVICE_NAME_HEADER;

import sopt.makers.authentication.adapter.in.web.common.BaseResponse;
import sopt.makers.authentication.adapter.in.web.common.code.UserSuccess;
import sopt.makers.authentication.adapter.in.web.dto.user.request.UserRequest;
import sopt.makers.authentication.adapter.in.web.dto.user.response.UserResponse;
import sopt.makers.authentication.adapter.in.web.util.ResponseUtil;
import sopt.makers.authentication.application.port.in.user.GetUserProfileUsecase;
import sopt.makers.authentication.application.port.in.user.UpdateUserProfileUsecase;
import sopt.makers.authentication.domain.user.Part;
import sopt.makers.authentication.support.validator.UserIdValidator;
import sopt.makers.authentication.support.validator.UserSearchConditionValidator;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserApiController implements UserApi {
  private final GetUserProfileUsecase getUserProfileUsecase;
  private final UpdateUserProfileUsecase updateUserProfileUsecase;
  private final UserIdValidator userIdValidator;
  private final UserSearchConditionValidator userSearchConditionValidator;

  @GetMapping("")
  public ResponseEntity<BaseResponse<?>> getUserProfile(
      @RequestHeader(API_KEY_HEADER) String apiKey,
      @RequestHeader(SERVICE_NAME_HEADER) String serviceName,
      @RequestParam List<Long> userIds) {
    userIdValidator.validateUserIds(userIds);
    List<GetUserProfileUsecase.UserProfileAndActivityInfo> userInformation =
        getUserProfileUsecase.getUserInformation(userIds);

    return ResponseUtil.success(
        UserSuccess.GET_USER_PROFILE, UserResponse.UserProfileAndActivity.from(userInformation));
  }

  @PutMapping("/{userId}")
  public ResponseEntity<BaseResponse<?>> updateUserProfile(
      @RequestHeader(API_KEY_HEADER) String apiKey,
      @RequestHeader(SERVICE_NAME_HEADER) String serviceName,
      @PathVariable Long userId,
      @Valid @RequestBody UserRequest.UserProfileInfo userProfileInfo) {
    userIdValidator.validateUserIds(userId);
    updateUserProfileUsecase.updateUserProfile(userProfileInfo.toCommand(userId));

    return ResponseUtil.success(UserSuccess.UPDATE_USER_PROFILE);
  }

  @GetMapping("/search")
  public ResponseEntity<BaseResponse<?>> getUserProfileByActivity(
      @RequestHeader(API_KEY_HEADER) String apiKey,
      @RequestHeader(SERVICE_NAME_HEADER) String serviceName,
      @RequestParam(required = false) Integer generation,
      @RequestParam(required = false) Part part) {
    userSearchConditionValidator.validateUserSearchCondition(generation, part);
    List<GetUserProfileUsecase.UserProfileAndActivityInfo> userInformation =
        getUserProfileUsecase.getUserInformationByActivity(generation, part);

    return ResponseUtil.success(
        UserSuccess.GET_USER_PROFILE, UserResponse.UserProfileAndActivity.from(userInformation));
  }

  @GetMapping("/count")
  public ResponseEntity<BaseResponse<?>> getUserCountByGeneration(
      @RequestHeader(API_KEY_HEADER) String apiKey,
      @RequestHeader(SERVICE_NAME_HEADER) String serviceName,
      @RequestParam int generation) {
    GetUserProfileUsecase.UserCountByGeneration count =
        getUserProfileUsecase.getUserCountByGeneration(generation);
    return ResponseUtil.success(
        UserSuccess.GET_USER_COUNT, UserResponse.UserCountByGeneration.from(count));
  }
}
