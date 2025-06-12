package sopt.makers.authentication.application.user.api;

import static sopt.makers.authentication.support.constant.SystemConstant.API_KEY_HEADER;
import static sopt.makers.authentication.support.constant.SystemConstant.SERVICE_NAME_HEADER;

import sopt.makers.authentication.application.user.dto.request.UserRequest;
import sopt.makers.authentication.application.user.dto.response.UserResponse;
import sopt.makers.authentication.support.code.domain.success.UserSuccess;
import sopt.makers.authentication.support.common.api.BaseResponse;
import sopt.makers.authentication.support.util.ResponseUtil;
import sopt.makers.authentication.support.validator.UserIdValidator;
import sopt.makers.authentication.usecase.user.port.in.GetUserProfileUsecase;
import sopt.makers.authentication.usecase.user.port.in.UpdateUserProfileUsecase;

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
}
