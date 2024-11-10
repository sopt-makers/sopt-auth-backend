package sopt.makers.authentication.domain.auth;

import java.util.*;

import jakarta.validation.constraints.*;

public record Activity(
    int generation, Optional<Team> team, Optional<Part> part, @NotNull Role role) {

  public static Activity of(int generation, final Team team, final Part part) {
    return new Activity(
        generation, Optional.ofNullable(team), Optional.ofNullable(part), Role.MEMBER);
  }

  public static Activity of(int generation, final Team team, final Part part, final Role role) {
    return new Activity(generation, Optional.ofNullable(team), Optional.ofNullable(part), role);
  }

  public void validateActivityContentsEmpty() {
    if (this.role.isPartRequired() && this.part.isEmpty()) {
      throw new IllegalArgumentException("해당 Role은 part 필드가 필수입니다.");
    }
  }
}
