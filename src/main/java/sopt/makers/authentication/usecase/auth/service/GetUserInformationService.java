package sopt.makers.authentication.usecase.auth.service;

import sopt.makers.authentication.domain.user.ActivityList;
import sopt.makers.authentication.domain.user.User;
import sopt.makers.authentication.usecase.auth.port.in.GetUserInformationUsecase;
import sopt.makers.authentication.usecase.auth.port.out.UserRepository;
import sopt.makers.authentication.usecase.user.port.out.UserActivityHistoryRepository;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetUserInformationService implements GetUserInformationUsecase {

  private final UserRepository userRepository;
  private final UserActivityHistoryRepository userActivityHistoryRepository;

  @Override
  public UserProfileAndActivityInfo getUserInformation(long userId) {

    User user = userRepository.findById(userId);
    ActivityList activityList = userActivityHistoryRepository.findByUser(user.getId());
    return UserProfileAndActivityInfo.of(user, activityList);
  }
}
