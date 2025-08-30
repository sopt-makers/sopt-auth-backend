package sopt.makers.authentication.domain.auth;

import static lombok.AccessLevel.PRIVATE;

import java.time.LocalDateTime;
import java.util.Random;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder(access = PRIVATE)
@RequiredArgsConstructor(access = PRIVATE)
@EqualsAndHashCode
public class PhoneVerification {

  private static final int VERIFICATION_EXPIRY_MINUTES = 3;

  private final Long id;
  private final String name;
  private final String phone;
  private final PhoneVerificationType verificationType;
  private final VerificationCode verificationCode;
  private final LocalDateTime createdAt;
  private final boolean isVerified;

  public static PhoneVerification of(String phone, PhoneVerificationType type, String code) {
    VerificationCode verificationCode = VerificationCode.of(code);
    return PhoneVerification.builder()
        .phone(phone)
        .verificationType(type)
        .verificationCode(verificationCode)
        .build();
  }

  public static PhoneVerification of(
      String name, String phone, PhoneVerificationType type, String code) {
    VerificationCode verificationCode = VerificationCode.of(code);
    return PhoneVerification.builder()
        .name(name)
        .phone(phone)
        .verificationType(type)
        .verificationCode(verificationCode)
        .createdAt(null)
        .isVerified(false)
        .build();
  }

  public static PhoneVerification of(
      Long id,
      String name,
      String phone,
      PhoneVerificationType type,
      String code,
      LocalDateTime createdAt,
      boolean isVerified) {
    VerificationCode verificationCode = VerificationCode.of(code);
    return PhoneVerification.builder()
        .id(id)
        .name(name)
        .phone(phone)
        .verificationCode(verificationCode)
        .verificationType(type)
        .createdAt(createdAt)
        .isVerified(isVerified)
        .build();
  }

  public static PhoneVerification create(String name, String phone, PhoneVerificationType type) {
    VerificationCode randomCode = VerificationCode.random();
    return PhoneVerification.builder()
        .name(name)
        .phone(phone)
        .verificationType(type)
        .verificationCode(randomCode)
        .createdAt(LocalDateTime.now())
        .isVerified(false)
        .build();
  }

  public boolean isExpired() {
    return LocalDateTime.now().isAfter(createdAt.plusMinutes(VERIFICATION_EXPIRY_MINUTES));
  }

  public PhoneVerification updateIsVerified() {
    return PhoneVerification.builder()
        .id(this.id)
        .name(this.name)
        .phone(this.phone)
        .verificationType(this.verificationType)
        .verificationCode(this.verificationCode)
        .createdAt(this.createdAt)
        .isVerified(true)
        .build();
  }

  @Getter
  @EqualsAndHashCode
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
