package sopt.makers.authentication.adapter.in.web.util;

import static sopt.makers.authentication.common.constant.SystemConstant.UTF_8;

import sopt.makers.authentication.adapter.in.web.common.BaseResponse;
import sopt.makers.authentication.common.code.FailureCode;
import sopt.makers.authentication.common.code.SuccessCode;
import sopt.makers.authentication.common.exception.BaseException;

import java.io.IOException;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public final class ResponseUtil {
  private final ObjectMapper objectMapper;

  public void generateErrorResponse(
      final HttpServletResponse response, final BaseException exception) throws IOException {
    String bodyValue =
        objectMapper.writeValueAsString(BaseResponse.ofFailure(exception.getError()));

    response.setStatus(exception.getError().getStatus().value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding(UTF_8);
    response.getWriter().write(bodyValue);
  }

  public static <T> ResponseEntity<BaseResponse<?>> success(SuccessCode code, T data) {
    return ResponseEntity.status(code.getStatus()).body(BaseResponse.ofSuccess(code, data));
  }

  public static ResponseEntity<BaseResponse<?>> success(SuccessCode code) {
    return ResponseEntity.status(code.getStatus()).body(BaseResponse.ofSuccess(code));
  }

  public static <T> ResponseEntity<BaseResponse<?>> success(
      SuccessCode code, HttpHeaders headers, T data) {
    return ResponseEntity.status(code.getStatus())
        .headers(headers)
        .body(BaseResponse.ofSuccess(code, data));
  }

  public static <T> ResponseEntity<BaseResponse<?>> failure(FailureCode code, T data) {
    return ResponseEntity.status(code.getStatus()).body(BaseResponse.ofFailure(code, data));
  }

  public static ResponseEntity<BaseResponse<?>> failure(FailureCode code) {
    return ResponseEntity.status(code.getStatus()).body(BaseResponse.ofFailure(code));
  }
}
