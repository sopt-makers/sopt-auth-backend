package sopt.makers.authentication.domain.user;

import static sopt.makers.authentication.support.code.domain.failure.UserFailure.NOT_FOUND_PART;

import sopt.makers.authentication.support.exception.domain.UserException;

import java.util.Arrays;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Part {
  ANDROID("안드로이드"),
  IOS("iOS"),
  SERVER("서버"),
  DESIGN("디자인"),
  PLAN("기획"),
  WEB("웹");

  private final String name;

  public static Part findPart(final String part) {

    return Arrays.stream(Part.values())
        .filter(p -> p.name().equals(part))
        .findFirst()
        .orElseThrow(() -> new UserException(NOT_FOUND_PART));
  }
}
