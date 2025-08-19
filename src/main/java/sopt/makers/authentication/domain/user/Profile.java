package sopt.makers.authentication.domain.user;

import java.time.LocalDate;
import java.util.Optional;

import jakarta.validation.constraints.NotNull;

public record Profile(
    @NotNull String name,
    Optional<String> email,
    @NotNull String phone,
    @NotNull LocalDate birthday,
    Optional<String> profileImage) {

  public static Profile of(String name, String email, String phone, LocalDate birthday) {
    return new Profile(name, Optional.ofNullable(email), phone, birthday, Optional.empty());
  }

  public static Profile of(
      String name, String email, String phone, LocalDate birthday, String profileImage) {
    return new Profile(
        name, Optional.ofNullable(email), phone, birthday, Optional.ofNullable(profileImage));
  }

  public Profile updateProfile(
      String email, String phone, LocalDate birthday, String profileImage) {
    return new Profile(
        this.name, Optional.ofNullable(email), phone, birthday, Optional.ofNullable(profileImage));
  }
}
