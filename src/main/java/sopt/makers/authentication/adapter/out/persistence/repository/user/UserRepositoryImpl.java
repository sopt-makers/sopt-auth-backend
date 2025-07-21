package sopt.makers.authentication.adapter.out.persistence.repository.user;

import sopt.makers.authentication.adapter.out.persistence.entity.UserEntity;
import sopt.makers.authentication.application.port.out.user.UserRepository;
import sopt.makers.authentication.domain.auth.SocialAccount;
import sopt.makers.authentication.domain.user.Part;
import sopt.makers.authentication.domain.user.Profile;
import sopt.makers.authentication.domain.user.User;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
  private final UserRetriever userRetriever;
  private final UserRegister userRegister;

  @Override
  public User findBySocialAccount(SocialAccount socialAccount) {
    return userRetriever.findBySocialAccount(socialAccount);
  }

  @Override
  public User findById(Long userId) {
    return userRetriever.findById(userId).toDomain();
  }

  @Override
  public List<User> findAllById(List<Long> userIds) {
    return userRetriever.findAllById(userIds);
  }

  @Transactional
  @Override
  public User save(User user) {
    UserEntity userEntity = UserEntity.fromDomain(user);
    userRegister.save(userEntity);
    return userEntity.toDomain();
  }

  @Override
  public User findByPhone(String phone) {
    return userRetriever.findByPhone(phone);
  }

  @Transactional
  @Override
  public void update(User user, SocialAccount socialAccount) {
    User updatedUser = user.updateSocialAccount(socialAccount);
    UserEntity userEntity = UserEntity.fromDomain(updatedUser);
    userRegister.save(userEntity);
  }

  @Transactional
  @Override
  public void update(User user, Profile profile) {
    User updatedUser = user.updateProfile(profile);
    UserEntity userEntity = UserEntity.fromDomain(updatedUser);
    userRegister.save(userEntity);
  }

  @Override
  public boolean existsByPhone(String phone) {
    return userRetriever.existsByPhone(phone);
  }

  @Override
  public Page<User> findAllByGenerationAndPartAndName(
      Integer generation, Part part, String name, Pageable pageable) {
    return userRetriever.findAllByGenerationAndPartAndName(generation, part, name, pageable);
  }

  @Override
  public int countByGeneration(int generation) {
    return userRetriever.countByGeneration(generation);
  }
}
