package sopt.makers.authentication.support.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = {"sopt.makers.authentication.database.postgres.entity"})
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = {"sopt.makers.authentication.database.postgres.repository"})
public class JpaConfig {}
