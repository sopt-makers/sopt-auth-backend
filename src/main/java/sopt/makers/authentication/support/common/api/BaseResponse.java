package sopt.makers.authentication.support.common.api;

import static lombok.AccessLevel.PRIVATE;

import sopt.makers.authentication.support.code.base.FailureCode;
import sopt.makers.authentication.support.code.base.SuccessCode;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder(access = PRIVATE)
@RequiredArgsConstructor(access = PRIVATE)
public class BaseResponse<T> {

  @JsonProperty("success")
  private final boolean isSuccess;

  @JsonProperty("message")
  private final String message;

  @JsonProperty("data")
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
