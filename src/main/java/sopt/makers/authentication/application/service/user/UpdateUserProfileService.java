package sopt.makers.authentication.application.service.user;

import static sopt.makers.authentication.support.code.domain.failure.UserFailure.NOT_FOUND_USER_ACTIVITY;

import sopt.makers.authentication.application.port.in.user.UpdateUserProfileUsecase;
import sopt.makers.authentication.application.port.out.user.UserActivityHistoryRepository;
import sopt.makers.authentication.application.port.out.user.UserRepository;
import sopt.makers.authentication.domain.auth.PhoneVerificationType;
import sopt.makers.authentication.domain.user.Activity;
import sopt.makers.authentication.domain.user.ActivityList;
import sopt.makers.authentication.domain.user.Profile;
import sopt.makers.authentication.domain.user.Team;
import sopt.makers.authentication.domain.user.User;
import sopt.makers.authentication.support.exception.domain.UserException;
import sopt.makers.authentication.support.validator.PhoneVerificationValidator;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UpdateUserProfileService implements UpdateUserProfileUsecase {
  private final UserRepository userRepository;
  private final UserActivityHistoryRepository userActivityHistoryRepository;
  private final PhoneVerificationValidator phoneVerificationValidator;

  @Transactional
  @Override
  public void updateUserProfile(UserProfileCommand command) {
    User user = userRepository.findById(command.userId());
    if (!user.getProfile().phone().equals(command.phone())) {
      phoneVerificationValidator.validate(
          user.getProfile().name(), command.phone(), PhoneVerificationType.CHANGE_PHONE_NUMBER);
    }
    ActivityList activityList = userActivityHistoryRepository.findByUser(user.getId());
    Profile updatedProfile =
        user.getProfile()
            .updateProfile(
                command.email(), command.phone(),
                command.birthday(), command.profileImage());
    Map<Long, Activity> existingActivitiesById = findExistsActivities(activityList);
    List<Activity> updatedActivities = updateActivities(command, existingActivitiesById);

    userActivityHistoryRepository.update(user, ActivityList.of(updatedActivities));
    userRepository.update(user, updatedProfile);
  }

  private Map<Long, Activity> findExistsActivities(ActivityList activityList) {
    return activityList.getActivities().stream()
        .collect(Collectors.toMap(Activity::getId, Function.identity()));
  }

  private List<Activity> updateActivities(
      UserProfileCommand command, Map<Long, Activity> existingActivitiesById) {
    return command.soptActivities().stream()
        .map(
            c -> {
              Activity existing = existingActivitiesById.get(c.activityId());
              if (existing == null) {
                throw new UserException(NOT_FOUND_USER_ACTIVITY);
              }
              return Activity.of(
                  c.activityId(),
                  existing.getGeneration(),
                  c.team() == null ? null : Team.findTeam(c.team()),
                  existing.getPart(),
                  existing.getRole());
            })
        .toList();
  }
}
