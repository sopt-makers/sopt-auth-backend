package sopt.makers.authentication;

import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = {"classpath:env/test.env"})
@ConfigurationPropertiesScan(basePackageClasses = AuthenticationApplication.class)
class AuthenticationApplicationTests {

  @Test
  void contextLoads() {}
}
