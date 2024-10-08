package sopt.makers.authentication.support.util.dto;

import lombok.Builder;

import static lombok.AccessLevel.*;


@Builder(access = PRIVATE)
public record BaseResponse <T> (
        boolean success,
        String message,
        T data
) {
    private static final boolean SUCCESS = true;
    private static final boolean FAILURE = false;

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
