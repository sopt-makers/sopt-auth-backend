package sopt.makers.authentication.adapter.out.persistence.entity;

import static lombok.AccessLevel.PROTECTED;

import sopt.makers.authentication.domain.user.Part;
import sopt.makers.authentication.domain.user.UserRegisterInfo;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Table(name = "user_register_infos")
public class UserRegisterInfoEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull private String name;
  @NotNull private String phone;
  @NotNull private String email;
  @NotNull private LocalDate birthday;

  @Min(1)
  private int generation;

  @NotNull
  @Enumerated(EnumType.STRING)
  private Part part;

  private UserRegisterInfoEntity(
      final String name,
      final String phone,
      final String email,
      final LocalDate birthday,
      final int generation,
      final Part part) {
    this.name = name;
    this.phone = phone;
    this.email = email;
    this.birthday = birthday;
    this.generation = generation;
    this.part = part;
  }

  public static UserRegisterInfoEntity fromDomain(UserRegisterInfo userRegisterInfo) {
    UserRegisterInfoEntity entity =
        new UserRegisterInfoEntity(
            userRegisterInfo.getName(),
            userRegisterInfo.getPhone(),
            userRegisterInfo.getEmail(),
            userRegisterInfo.getBirthday(),
            userRegisterInfo.getGeneration(),
            userRegisterInfo.getPart());
    return entity;
  }

  public UserRegisterInfo toDomain() {
    return UserRegisterInfo.of(
        this.name, this.phone, this.email, this.birthday, this.generation, this.part);
  }
}
