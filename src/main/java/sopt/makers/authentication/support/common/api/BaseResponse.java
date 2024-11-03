package sopt.makers.authentication.support.common.api;

import static lombok.AccessLevel.PRIVATE;

import sopt.makers.authentication.support.common.code.base.FailureCode;
import sopt.makers.authentication.support.common.code.base.SuccessCode;

import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Builder(access = PRIVATE)
@RequiredArgsConstructor(access = PRIVATE)
public class BaseResponse<T> {

  private final boolean isSuccess;
  private final String message;
  private final T data;

  public static <T> BaseResponse<?> ofFailure(FailureCode failure, T data) {
    return new BaseResponse<>(false, failure.getMessage(), data);
  }

  public static BaseResponse<?> ofFailure(FailureCode failure) {
    return new BaseResponse<>(false, failure.getMessage(), null);
  }

  public static <T> BaseResponse<?> ofSuccess(SuccessCode success, T data) {
    return new BaseResponse<>(true, success.getMessage(), data);
  }

  public static BaseResponse<?> ofSuccess(SuccessCode success) {
    return new BaseResponse<>(true, success.getMessage(), null);
  }
}
