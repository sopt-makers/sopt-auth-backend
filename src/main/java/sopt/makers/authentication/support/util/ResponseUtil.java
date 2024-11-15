package sopt.makers.authentication.support.util;

import static sopt.makers.authentication.support.constant.SystemConstant.UTF_8;

import sopt.makers.authentication.support.common.api.BaseResponse;
import sopt.makers.authentication.support.exception.base.BaseException;

import java.io.IOException;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;

public final class ResponseUtil {
  private ResponseUtil() {}

  private static final ObjectMapper MAPPER = new ObjectMapper();

  public static void generateErrorResponse(
      final HttpServletResponse response, final BaseException exception) throws IOException {
    String bodyValue = MAPPER.writeValueAsString(BaseResponse.ofFailure(exception.getError()));

    response.setStatus(exception.getError().getStatus().value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding(UTF_8);
    response.getWriter().write(bodyValue);
  }
}
