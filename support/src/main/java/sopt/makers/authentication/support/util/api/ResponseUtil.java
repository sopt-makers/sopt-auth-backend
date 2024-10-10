package sopt.makers.authentication.support.util.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import sopt.makers.authentication.support.common.exception.BaseException;

import java.io.IOException;

import static sopt.makers.authentication.support.value.SystemConstant.UTF_8;


public final class ResponseUtil {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    public static void generateErrorResponse(HttpServletResponse response, BaseException exception) throws IOException {
        String bodyValue = MAPPER.writeValueAsString(ResponseGenerator.failureOf(exception.getFailure()));

        response.setStatus(exception.getFailure().getStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(UTF_8);
        response.getWriter().write(bodyValue);
    }
}
