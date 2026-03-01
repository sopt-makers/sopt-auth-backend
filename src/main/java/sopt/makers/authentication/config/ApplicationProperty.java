package sopt.makers.authentication.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring")
public record ApplicationProperty(Datasource datasource, Redis redis, Web web) {
  public record Datasource(
      String url, String username, String password, String driverClassName, Hikari hikari) {
    public record Hikari(
        Integer maximumPoolSize,
        Integer minimumIdle,
        Long connectionTimeout,
        Long idleTimeout,
        Long maxLifetime,
        Long validationTimeout,
        Long leakDetectionThreshold,
        String connectionTestQuery,
        Boolean registerMbeans) {}
  }

  public record Redis(String host, Integer port, String password) {}

  public record Web(Resources resources) {
    public record Resources(Boolean addMappings) {}
  }
}
