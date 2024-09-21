package sopt.makers.authentication;

import sopt.makers.authentication.infra.InfraRoot;
import sopt.makers.authentication.support.SupportRoot;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

@SpringBootTest
@ComponentScan(basePackageClasses = {DomainRoot.class, InfraRoot.class, SupportRoot.class})
class AppApplicationTests {

@Test
void 전체_컨텍스트_로드_테스트() {}
}
