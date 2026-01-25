package sopt.makers.authentication.domain.user;

import static sopt.makers.authentication.domain.user.exception.UserFailure.NOT_FOUND_PART;

import sopt.makers.authentication.domain.user.exception.UserException;

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
  WEB("웹"),

  // SOPT MAKERS 챕터
  PM("PM"),
  FRONTEND("프론트엔드"),
  BACKEND("백엔드"),
  MARKETING("마케팅"),
  CX("CX");

  private final String name;

  public static Part findPart(final String part) {
    return Arrays.stream(Part.values())
        .filter(p -> p.name.equals(part))
        .findFirst()
        .orElseThrow(() -> new UserException(NOT_FOUND_PART));
  }
}
