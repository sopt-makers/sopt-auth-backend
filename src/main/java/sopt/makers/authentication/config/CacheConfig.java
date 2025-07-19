package sopt.makers.authentication.config;

import java.time.Duration;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Caffeine;

@Configuration
@EnableCaching
public class CacheConfig {
  private static final int EXPIRE_DAY = 1;
  private static final int MAXIMUM_SIZE = 3000;
  private static final String CACHE_NAME = "users";

  @Bean
  public CacheManager cacheManager() {
    CaffeineCacheManager cacheManager = new CaffeineCacheManager(CACHE_NAME);
    cacheManager.setCaffeine(
        Caffeine.newBuilder()
            .expireAfterWrite(Duration.ofDays(EXPIRE_DAY))
            .maximumSize(MAXIMUM_SIZE));
    return cacheManager;
  }
}
