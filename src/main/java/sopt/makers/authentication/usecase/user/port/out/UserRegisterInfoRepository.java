package sopt.makers.authentication.usecase.user.port.out;

import sopt.makers.authentication.domain.user.UserRegisterInfo;

public interface UserRegisterInfoRepository {

  UserRegisterInfo findByPhone(String phone);

  void delete(UserRegisterInfo userRegisterInfo);
}
