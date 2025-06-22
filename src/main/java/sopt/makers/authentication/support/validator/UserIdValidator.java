package sopt.makers.authentication.support.validator;

import static sopt.makers.authentication.domain.user.exception.UserFailure.NOT_VALID_USER_ID;

import sopt.makers.authentication.domain.user.exception.UserException;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class UserIdValidator {

  public void validateUserIds(List<Long> userIds) {
    if (userIds != null && userIds.stream().anyMatch(id -> id <= 0)) {
      throw new UserException(NOT_VALID_USER_ID);
    }
  }

  public void validateUserIds(Long userId) {
    if (userId <= 0) {
      throw new UserException(NOT_VALID_USER_ID);
    }
  }
}
