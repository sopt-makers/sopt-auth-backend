package sopt.makers.authentication.domain.user;

import static lombok.AccessLevel.PRIVATE;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder(access = PRIVATE)
@RequiredArgsConstructor(access = PRIVATE)
public class UserRegisterInfo {
  private final String name;
  private final String phone;
  private final String email;
  private final LocalDate birthday;
  private final int generation;
  private final Part part;

  public static UserRegisterInfo of(
      String name, String phone, String email, LocalDate birthday, int generation, Part part) {
    return new UserRegisterInfo(name, phone, email, birthday, generation, part);
  }
}
