package sopt.makers.authentication.database.core.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "phone_verifications")
public class PhoneVerification {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  Long id;
}
