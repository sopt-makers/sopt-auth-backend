package sopt.makers.authentication.adapter.out.external.exception.ClientException;

import sopt.makers.authentication.adapter.out.external.exception.ClientError;
import sopt.makers.authentication.common.exception.BaseException;

public class S3Exception extends BaseException {
  public S3Exception(ClientError failure) {
    super(failure);
  }
}
