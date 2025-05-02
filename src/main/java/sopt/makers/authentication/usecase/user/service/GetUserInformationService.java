package sopt.makers.authentication.usecase.user.service;

import sopt.makers.authentication.domain.user.ActivityList;
import sopt.makers.authentication.domain.user.User;
import sopt.makers.authentication.usecase.user.port.in.GetUserInformationUsecase;
import sopt.makers.authentication.usecase.user.port.out.UserActivityHistoryRepository;
import sopt.makers.authentication.usecase.user.port.out.UserRepository;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetUserInformationService implements GetUserInformationUsecase {

  private final UserRepository userRepository;
  private final UserActivityHistoryRepository userActivityHistoryRepository;

  @Override
  public List<UserProfileAndActivityInfo> getUserInformation(List<Long> userId) {
    List<User> userList = userRepository.findAllById(userId);

    Map<Long, ActivityList> activityMap = userActivityHistoryRepository.findAllByUserIdIn(userId);

    List<UserProfileAndActivityInfo> userProfileAndActivityInfos =
        userList.stream()
            .map(user -> UserProfileAndActivityInfo.of(user, activityMap.get(user.getId())))
            .toList();

    return userProfileAndActivityInfos;
  }
}
