package sopt.makers.authentication.domain.auth;

import static lombok.AccessLevel.PRIVATE;

import java.util.Random;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder(access = PRIVATE)
@RequiredArgsConstructor(access = PRIVATE)
public class PhoneVerification {

  private final Long id;
  private final String name;
  private final String phone;
  private final PhoneVerificationType verificationType;
  private final VerificationCode verificationCode;

  public static PhoneVerification of(
      Long id, String name, String phone, PhoneVerificationType type, String code) {
    VerificationCode verificationCode = VerificationCode.of(code);
    return PhoneVerification.builder()
        .id(id)
        .name(name)
        .phone(phone)
        .verificationType(type)
        .verificationCode(verificationCode)
        .build();
  }

  public static PhoneVerification create(String name, String phone, PhoneVerificationType type) {
    VerificationCode randomCode = VerificationCode.random();
    return PhoneVerification.builder()
        .name(name)
        .phone(phone)
        .verificationType(type)
        .verificationCode(randomCode)
        .build();
  }

  @Getter
  public static class VerificationCode {
    private static final int CODE_SIZE = 6;
    private final String code;

    private VerificationCode(final String code) {
      this.code = code;
    }

    private static VerificationCode random() {
      return new VerificationCode(generateRandomCode());
    }

    private static VerificationCode of(String code) {
      return new VerificationCode(code);
    }

    private static String generateRandomCode() {
      Random random = new Random();
      StringBuilder codeBuilder = new StringBuilder(CODE_SIZE);
      for (int seq = 0; seq < CODE_SIZE; seq++) {
        codeBuilder.append(random.nextInt(10));
      }
      return codeBuilder.toString();
    }
  }
}
