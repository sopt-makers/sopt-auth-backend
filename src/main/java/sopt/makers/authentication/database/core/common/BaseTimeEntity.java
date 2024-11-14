package sopt.makers.authentication.database.core.common;

import java.time.*;

import jakarta.persistence.*;

import org.springframework.data.annotation.*;
import org.springframework.data.jpa.domain.support.*;

import lombok.*;

@Getter
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class BaseTimeEntity {
  @CreatedDate
  @Column(updatable = false)
  private LocalDateTime createdAt;

  @LastModifiedDate private LocalDateTime updatedAt;
}
