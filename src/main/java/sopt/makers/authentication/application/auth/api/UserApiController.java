package sopt.makers.authentication.application.auth.api;

import sopt.makers.authentication.application.auth.dto.response.UserResponse;
import sopt.makers.authentication.support.code.domain.success.UserSuccess;
import sopt.makers.authentication.support.common.api.BaseResponse;
import sopt.makers.authentication.support.util.ResponseUtil;
import sopt.makers.authentication.usecase.auth.port.in.GetUserInformationUsecase;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserApiController implements UserApi {
  private final GetUserInformationUsecase getUserInformationUsecase;

  @GetMapping("/{userId}")
  public ResponseEntity<BaseResponse<?>> getUserInformation(@PathVariable long userId) {
    GetUserInformationUsecase.UserProfileAndActivityInfo userInformation =
        getUserInformationUsecase.getUserInformation(userId);

    return ResponseUtil.success(
        UserSuccess.GET_USER_INFORMATION,
        UserResponse.UserProfileAndActivity.from(userInformation));
  }
}
