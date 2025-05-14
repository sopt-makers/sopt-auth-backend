package sopt.makers.authentication.usecase.user.service;

import sopt.makers.authentication.domain.user.User;
import sopt.makers.authentication.usecase.user.port.in.GetUserProfileUsecase;
import sopt.makers.authentication.usecase.user.port.out.UserActivityHistoryRepository;
import sopt.makers.authentication.usecase.user.port.out.UserRepository;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetUserProfileService implements GetUserProfileUsecase {

  private final UserRepository userRepository;
  private final UserActivityHistoryRepository userActivityHistoryRepository;

  @Override
  public List<UserProfileAndActivityInfo> getUserInformation(List<Long> userIds) {
    List<User> userList = userRepository.findAllById(userIds);
    List<UserProfileAndActivityInfo> userProfileAndActivityInfos =
        userList.stream()
            .map(user -> UserProfileAndActivityInfo.of(user, user.getActivities()))
            .toList();

    return userProfileAndActivityInfos;
  }
}
