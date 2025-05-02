package sopt.makers.authentication.application.user.api;

import static sopt.makers.authentication.support.constant.SystemConstant.API_KEY_HEADER;
import static sopt.makers.authentication.support.constant.SystemConstant.SERVICE_NAME_HEADER;

import sopt.makers.authentication.application.user.dto.response.UserResponse;
import sopt.makers.authentication.support.code.domain.success.UserSuccess;
import sopt.makers.authentication.support.common.api.BaseResponse;
import sopt.makers.authentication.support.util.ResponseUtil;
import sopt.makers.authentication.support.validator.UserIdValidator;
import sopt.makers.authentication.usecase.user.port.in.GetUserInformationUsecase;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserApiController implements UserApi {
  private final GetUserInformationUsecase getUserInformationUsecase;
  private final UserIdValidator userIdValidator;

  @GetMapping("/users")
  public ResponseEntity<BaseResponse<?>> getUserInformation(
      @RequestHeader(API_KEY_HEADER) String apiKey,
      @RequestHeader(SERVICE_NAME_HEADER) String serviceName,
      @RequestParam List<Long> userId) {
    userIdValidator.validateUserIds(userId);

    List<GetUserInformationUsecase.UserProfileAndActivityInfo> userInformation =
        getUserInformationUsecase.getUserInformation(userId);

    return ResponseUtil.success(
        UserSuccess.GET_USER_INFORMATION,
        UserResponse.UserProfileAndActivity.from(userInformation));
  }
}
