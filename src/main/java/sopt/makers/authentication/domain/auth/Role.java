package sopt.makers.authentication.domain.auth;

import static sopt.makers.authentication.support.common.code.failure.DomainFailure.NOT_FOUND_ROLE;

import sopt.makers.authentication.support.common.exception.DomainException;

import java.util.Arrays;

public enum Role {
  MEMBER,
  PRESIDENT,
  VICE_PRESIDENT,
  TEAM_LEADER,
  PART_LEADER;

  public static Role findRole(final String role) {
    return Arrays.stream(Role.values())
        .filter(r -> r.name().equals(role))
        .findFirst()
        .orElseThrow(() -> new DomainException(NOT_FOUND_ROLE));
  }

  public boolean isPartRequired() {
    return !(this == PRESIDENT || this == VICE_PRESIDENT);
  }
}
