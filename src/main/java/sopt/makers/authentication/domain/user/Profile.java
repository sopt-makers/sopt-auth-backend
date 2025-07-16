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

  public Profile updateName(final String name) {
    return new Profile(name, this.email, this.phone, this.birthday, this.profileImage);
  }

  public Profile updateEmail(final String email) {
    return new Profile(
        this.name, Optional.ofNullable(email), this.phone, this.birthday, this.profileImage);
  }

  public Profile updatePhone(final String phone) {
    return new Profile(this.name, this.email, phone, this.birthday, this.profileImage);
  }

  public Profile updateBirthday(final LocalDate birthday) {
    return new Profile(this.name, this.email, this.phone, birthday, this.profileImage);
  }

  public Profile updateProfileImage(final String profileImage) {
    return new Profile(
        this.name, this.email, this.phone, this.birthday, Optional.ofNullable(profileImage));
  }
}
