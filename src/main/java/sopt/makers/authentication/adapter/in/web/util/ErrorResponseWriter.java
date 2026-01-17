package sopt.makers.authentication.adapter.in.web.util;

import static sopt.makers.authentication.common.constant.SystemConstant.UTF_8;

import sopt.makers.authentication.adapter.in.web.common.BaseResponse;
import sopt.makers.authentication.common.exception.BaseException;

import java.io.IOException;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ErrorResponseWriter {
  private final ObjectMapper objectMapper;

  public void write(final HttpServletResponse response, final BaseException exception)
      throws IOException {
    String bodyValue =
        objectMapper.writeValueAsString(BaseResponse.ofFailure(exception.getError()));

    response.setStatus(exception.getError().getStatus().value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding(UTF_8);
    response.getWriter().write(bodyValue);
  }
}
