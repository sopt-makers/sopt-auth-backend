package sopt.makers.authentication.domain.auth;

import java.time.LocalDate;

import jakarta.validation.constraints.*;

public record Profile(
    @NotNull String name, @NotNull String email, @NotNull String phone, LocalDate birthday) {
  public static Profile of(String name, String email, String phone, LocalDate birthday) {
    return new Profile(name, email, phone, birthday);
  }

  public Profile updateName(final String name) {
    return new Profile(name, this.email, this.phone, this.birthday);
  }

  public Profile updateEmail(final String email) {
    return new Profile(this.name, email, this.phone, this.birthday);
  }

  public Profile updatePhone(final String phone) {
    return new Profile(this.name, this.email, phone, this.birthday);
  }

  public Profile updateBirthday(final LocalDate birthday) {
    return new Profile(this.name, this.email, this.phone, birthday);
  }
}
