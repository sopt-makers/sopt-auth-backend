package sopt.makers.authentication.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import lombok.RequiredArgsConstructor;

@Configuration
@Profile("!test")
@RequiredArgsConstructor
public class RedissonConfig {

  private final ApplicationProperty applicationProperty;

  @Bean(destroyMethod = "shutdown")
  public RedissonClient redissonClient() {

    Config config = new Config();
    String host = applicationProperty.redis().host();
    Integer port = applicationProperty.redis().port();
    String password = applicationProperty.redis().password();

    config
        .useSingleServer()
        .setAddress("redis://" + host + ":" + port)
        .setPassword(password == null || password.isBlank() ? null : password)
        .setDatabase(0)
        .setConnectionPoolSize(10)
        .setConnectionMinimumIdleSize(2)
        .setTimeout(3000)
        .setConnectTimeout(2000)
        .setRetryAttempts(3)
        .setRetryInterval(1500);

    return Redisson.create(config);
  }
}
