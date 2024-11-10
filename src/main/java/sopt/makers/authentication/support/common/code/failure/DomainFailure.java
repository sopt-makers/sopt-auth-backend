package sopt.makers.authentication.support.common.code.failure;

import sopt.makers.authentication.support.common.code.base.*;

import org.springframework.http.*;

import lombok.*;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum DomainFailure implements FailureCode {
  ;
  private final HttpStatus status;
  private final String message;
}
