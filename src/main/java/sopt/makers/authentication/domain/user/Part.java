package sopt.makers.authentication.domain.user;

import static sopt.makers.authentication.support.code.domain.failure.UserFailure.NOT_FOUND_PART;

import sopt.makers.authentication.support.exception.domain.UserException;

import java.util.Arrays;

public enum Part {
  ANDROID,
  IOS,
  SERVER,
  DESIGN,
  PLAN,
  WEB;

  public static Part findPart(final String part) {

    return Arrays.stream(Part.values())
        .filter(p -> p.name().equals(part))
        .findFirst()
        .orElseThrow(() -> new UserException(NOT_FOUND_PART));
  }
}
