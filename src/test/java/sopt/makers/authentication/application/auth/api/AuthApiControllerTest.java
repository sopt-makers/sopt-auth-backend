package sopt.makers.authentication.application.auth.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static sopt.makers.authentication.usecase.auth.port.in.VerifyPhoneVerificationUsecase.*;

import sopt.makers.authentication.usecase.auth.port.in.CreatePhoneVerificationUsecase;
import sopt.makers.authentication.usecase.auth.port.in.VerifyPhoneVerificationUsecase;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {AuthApiController.class})
@WebMvcTest(
    controllers = {AuthApiController.class},
    excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@ActiveProfiles("test")
class AuthApiControllerTest {
  @Autowired MockMvc mockMvc;
  @MockBean VerifyPhoneVerificationUsecase verifyPhoneVerificationUsecase;
  @MockBean CreatePhoneVerificationUsecase createPhoneVerificationUsecase;

  @Test
  void verifyPhoneVerification() throws Exception {
    // given
    String givenRequestBodyContent =
        "{\"name\":\"TEST\",\"phone\":\"01012345678\",\"code\":\"123456\",\"type\":\"REGISTER\"}";

    // when
    when(verifyPhoneVerificationUsecase.verify(any(VerifyVerificationCommand.class)))
        .thenReturn(true);
    mockMvc
        .perform(
            post("/api/v1/auth/verify/phone")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("UTF-8")
                .content(givenRequestBodyContent))
        // then
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value("true"))
        .andExpect(jsonPath("$.message").value("번호 인증에 성공했습니다."))
        .andExpect(jsonPath("$.data.isVerified").value("true"));
  }
}
