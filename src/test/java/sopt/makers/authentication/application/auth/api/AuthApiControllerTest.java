package sopt.makers.authentication.application.auth.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static sopt.makers.authentication.usecase.auth.port.in.VerifyPhoneVerificationUsecase.*;

import sopt.makers.authentication.application.auth.dto.request.AuthRequest;
import sopt.makers.authentication.domain.auth.PhoneVerificationType;
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.fasterxml.jackson.databind.ObjectMapper;

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
    // String name, String number, String code, String verificationTypeName
    AuthRequest.VerifyPhoneVerification request =
        new AuthRequest.VerifyPhoneVerification(
            "TEST", "01012345678", "123456", PhoneVerificationType.REGISTER.name());
    String requestBody =
        "{\"name\":\"TEST\",\"phone\":\"01012345678\",\"code\":\"123456\",\"type\":\"REGISTER\"}";
    ObjectMapper mapper = new ObjectMapper();
    String givenRequestBody = mapper.writeValueAsString(request);
    System.out.println(givenRequestBody);
    //        VerifyVerificationCommand givenCommand = new VerifyVerificationCommand("TEST",
    // "01012345678", "123456", PhoneVerificationType.REGISTER);
    //        mock(AuthRequest.VerifyPhoneVerification.class);
    //        when(new AuthRequest.VerifyPhoneVerification(anyString(), anyString(), anyString(),
    // anyString()))
    //                .thenReturn(mock(AuthRequest.VerifyPhoneVerification.class));
    //                .thenReturn(request);
    //        when(new VerifyVerificationCommand(anyString(), anyString(), anyString(),
    // any(PhoneVerificationType.class)))
    //                .thenReturn(mock(VerifyVerificationCommand.class));
    //        String requestContent = mapper.writeValueAsString(request);
    //
    // doReturn(true).when(verifyPhoneVerificationUsecase.verify(any(VerifyVerificationCommand.class)));
    //        PhoneVerificationType mockedType = mock(PhoneVerificationType.class);
    //        AuthRequest.VerifyPhoneVerification mockedRequest =
    // mock(AuthRequest.VerifyPhoneVerification.class);
    //        given(mockedRequest.name()).willReturn("TEST");
    //        given(mockedRequest.number()).willReturn("01012345678");
    //        given(mockedRequest.code()).willReturn("123456");
    //        given(mockedRequest.verificationTypeName()).willReturn("REGISTER");
    //
    //        given(mockedRequest.toCommand())
    //                .willReturn(request.toCommand());

    //
    // when(PhoneVerificationType.getValue(anyString())).thenReturn(PhoneVerificationType.REGISTER);
    when(verifyPhoneVerificationUsecase.verify(any(VerifyVerificationCommand.class)))
        .thenReturn(true);
    //        given(verifyPhoneVerificationUsecase.verify(any(VerifyVerificationCommand.class)))
    //                .willReturn(true);

    // when
    mockMvc
        .perform(
            post("/api/v1/auth/verify/phone")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("UTF-8")
                .content(requestBody))
        // then
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.isSuccess").value("true"))
        .andExpect(jsonPath("$.message").value("번호 인증에 성공했습니다."))
        .andExpect(jsonPath("$.data.isVerified").value("true"))
        .andDo(MockMvcResultHandlers.print());
  }
}
