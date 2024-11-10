package sopt.makers.authentication.support.common.code.failure;

import sopt.makers.authentication.support.common.code.base.*;

import org.springframework.http.*;

import lombok.*;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum DomainFailure implements FailureCode {
  NOT_FOUND_ROLE(HttpStatus.NOT_FOUND, "존재하지 않는 역할입니다"),
  NOT_FOUND_PART(HttpStatus.NOT_FOUND, "존재하지 않는 파트입니다");
  private final HttpStatus status;
  private final String message;
}
