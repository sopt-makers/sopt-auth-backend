package sopt.makers.authentication.adapter.out.cache.exception;

import static lombok.AccessLevel.PRIVATE;

import sopt.makers.authentication.common.code.FailureCode;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public enum CacheFailure implements FailureCode {
  // 500
  CACHE_NOT_CONFIGURED(HttpStatus.INTERNAL_SERVER_ERROR, "캐시가 설정되지 않았습니다");

  private final HttpStatus status;
  private final String message;
}
