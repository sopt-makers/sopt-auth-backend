package sopt.makers.authentication.support.util.api;

import org.springframework.http.ResponseEntity;
import sopt.makers.authentication.support.common.code.Failure;
import sopt.makers.authentication.support.common.code.Success;
import sopt.makers.authentication.support.util.api.dto.BaseResponse;

public interface ResponseGenerator {
    static <T> ResponseEntity<BaseResponse<?>> successOf(Success success, T data) {
        return ResponseEntity
                .status(success.getStatus())
                .body(BaseResponse.ofSuccess(success.getMessage(), data));
    }
    static <T> ResponseEntity<BaseResponse<?>> successOf(Success success) {
        return ResponseEntity
                .status(success.getStatus())
                .body(BaseResponse.ofSuccess(success.getMessage()));
    }

    static <T> ResponseEntity<BaseResponse<?>> failureOf(Failure failure, T data) {
        return ResponseEntity
                .status(failure.getStatus())
                .body(BaseResponse.ofFailure(failure.getMessage(), data));
    }

    static <T> ResponseEntity<BaseResponse<?>> failureOf(Failure failure) {
        return ResponseEntity
                .status(failure.getStatus())
                .body(BaseResponse.ofFailure(failure.getMessage()));
    }

}
