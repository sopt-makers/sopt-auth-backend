package sopt.makers.authentication.application.service.user;

import static sopt.makers.authentication.domain.user.exception.UserFailure.NOT_FOUND_USER_ACTIVITY;

import sopt.makers.authentication.application.port.in.user.UpdateUserProfileUsecase;
import sopt.makers.authentication.application.port.out.user.UserActivityHistoryRepository;
import sopt.makers.authentication.application.port.out.user.UserCacheRepository;
import sopt.makers.authentication.application.port.out.user.UserRepository;
import sopt.makers.authentication.application.validator.auth.PhoneVerificationValidator;
import sopt.makers.authentication.domain.auth.PhoneVerificationType;
import sopt.makers.authentication.domain.user.Activity;
import sopt.makers.authentication.domain.user.ActivityList;
import sopt.makers.authentication.domain.user.Profile;
import sopt.makers.authentication.domain.user.Team;
import sopt.makers.authentication.domain.user.User;
import sopt.makers.authentication.domain.user.exception.UserException;

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
  private final UserCacheRepository userCacheRepository;

  @Transactional
  @Override
  public void updateUserProfile(UserProfileCommand command) {
    User user = userRepository.findByIdWithHistories(command.userId());
    validatePhoneIfChanged(user, command);

    Profile updatedProfile = updateUserProfile(user, command);
    ActivityList updatedActivities = updateUserActivities(user, command);
    updateUserCache(user, updatedProfile, updatedActivities);
  }

  private void validatePhoneIfChanged(User user, UserProfileCommand command) {
    boolean isPhoneChanged = !user.getProfile().phone().equals(command.phone());

    if (isPhoneChanged) {
      phoneVerificationValidator.validate(
          command.phone(), PhoneVerificationType.CHANGE_PHONE_NUMBER);
    }
  }

  private Profile updateUserProfile(User user, UserProfileCommand command) {
    Profile profile = user.getProfile();
    Profile updatedProfile =
        profile.updateProfile(
            command.email(), command.phone(), command.birthday(), command.profileImage());
    userRepository.update(user, updatedProfile);
    return updatedProfile;
  }

  private ActivityList updateUserActivities(User user, UserProfileCommand command) {
    Map<Long, Activity> existingActivitiesById = findExistsActivities(user.getActivities());
    List<Activity> updatedActivities = updateActivities(command, existingActivitiesById);
    userActivityHistoryRepository.update(user, ActivityList.of(updatedActivities));
    return ActivityList.of(updatedActivities);
  }

  private void updateUserCache(User user, Profile updatedProfile, ActivityList updatedActivities) {
    User updatedUser =
        User.createUser(
            user.getId(),
            user.getSocialAccount(),
            updatedProfile,
            updatedActivities,
            user.isFirstLogin());
    userCacheRepository.put(updatedUser);
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
