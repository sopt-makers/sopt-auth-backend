package sopt.makers.authentication.application.port.out.user;

import sopt.makers.authentication.domain.user.UserRegisterInfo;

import java.util.Optional;

public interface UserRegisterInfoRepository {

  Optional<UserRegisterInfo> findByPhone(String phone);

  void save(UserRegisterInfo userRegisterInfo);

  void delete(UserRegisterInfo userRegisterInfo);
}
