package sopt.makers.authentication.support.validator;

import static sopt.makers.authentication.domain.user.exception.UserFailure.BAD_REQUEST_INVALID_USER_SEARCH_CONDITION;

import sopt.makers.authentication.domain.user.Part;
import sopt.makers.authentication.domain.user.exception.UserException;

import org.springframework.stereotype.Component;

@Component
public class UserSearchConditionValidator {

  public void validateUserSearchCondition(Integer generation, Part part) {
    if (generation == null && part == null) {
      throw new UserException(BAD_REQUEST_INVALID_USER_SEARCH_CONDITION);
    }
  }
}
