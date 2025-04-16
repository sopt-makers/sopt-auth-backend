package sopt.makers.authentication.usecase.user.port.out;

import sopt.makers.authentication.domain.user.UserRegisterInfo;

import java.util.Optional;

public interface UserRegisterInfoRepository {

  Optional<UserRegisterInfo> findByPhone(String phone);

  void delete(UserRegisterInfo userRegisterInfo);
}
