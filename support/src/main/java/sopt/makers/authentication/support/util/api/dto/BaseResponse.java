package sopt.makers.authentication.support.util.api.dto;

import lombok.Builder;
import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.*;


@Builder(access = PRIVATE)
@RequiredArgsConstructor(access = PRIVATE)
public class BaseResponse <T> {

    private static final boolean SUCCESS = true;
    private static final boolean FAILURE = false;

    private final boolean isSuccess;
    private final String message;
    private final T data;

    public static <T> BaseResponse<?> ofSuccess(String message, T data) {
        return new BaseResponse<>(SUCCESS, message, data);
    }

    public static BaseResponse<?> ofSuccess(String message) {
        return new BaseResponse<>(SUCCESS, message, null);
    }

    public static <T> BaseResponse<?> ofFailure(String message, T data) {
        return new BaseResponse<>(FAILURE, message, data);
    }

    public static BaseResponse<?> ofFailure(String message) {
        return new BaseResponse<>(FAILURE, message, null);
    }

}
