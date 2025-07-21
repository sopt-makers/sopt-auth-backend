package sopt.makers.authentication.application.service.user;

import sopt.makers.authentication.application.port.in.user.GetUserProfileUsecase;
import sopt.makers.authentication.application.port.out.user.UserRepository;
import sopt.makers.authentication.domain.user.Part;
import sopt.makers.authentication.domain.user.User;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
  public PaginatedUserProfiles getUserInformationByFilters(
      Integer generation, Part part, String name, Integer offset, Integer limit) {
    Pageable pageable = PageRequest.of(offset / limit, limit, Sort.by("id").descending());
    Page<User> userEntityPage =
        userRepository.findAllByGenerationAndPartAndName(generation, part, name, pageable);

    int totalCount = (int) userEntityPage.getTotalElements();
    boolean hasNext = userEntityPage.hasNext();

    List<UserProfileAndActivityInfo> profiles =
        userEntityPage.getContent().stream()
            .map(user -> UserProfileAndActivityInfo.of(user, user.getActivities()))
            .toList();

    return new PaginatedUserProfiles(profiles, hasNext, totalCount);
  }

  @Override
  public UserCountByGeneration getUserCountByGeneration(int generation) {
    int count = userRepository.countByGeneration(generation);
    return new UserCountByGeneration(count);
  }
}
