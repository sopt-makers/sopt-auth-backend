package sopt.makers.authentication.usecase.user.service;

import sopt.makers.authentication.domain.user.Part;
import sopt.makers.authentication.domain.user.User;
import sopt.makers.authentication.usecase.user.port.in.GetUserProfileUsecase;
import sopt.makers.authentication.usecase.user.port.out.UserRepository;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetUserProfileService implements GetUserProfileUsecase {

  private final UserRepository userRepository;

  @Override
  public List<UserProfileAndActivityInfo> getUserInformation(List<Long> userIds) {
    List<User> userList = userRepository.findAllById(userIds);

    return userList.stream()
        .map(user -> UserProfileAndActivityInfo.of(user, user.getActivities()))
        .toList();
  }

  @Override
  public List<UserProfileAndActivityInfo> getUserInformationByActivity(
      Integer generation, Part part) {
    List<User> userList = userRepository.findAllByActivity(generation, part);
    return userList.stream()
        .map(user -> UserProfileAndActivityInfo.of(user, user.getActivities()))
        .toList();
  }

  @Override
  public UserCountByGeneration getUserCountByGeneration(int generation) {
    int count = userRepository.countByGeneration(generation);
    return new UserCountByGeneration(count);
  }
}
