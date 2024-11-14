package sopt.makers.authentication;

import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@ConfigurationPropertiesScan(basePackageClasses = AuthenticationApplication.class)
class AuthenticationApplicationTests {

  @Test
  void contextLoads() {}
}
