package sopt.makers.authentication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import sopt.makers.authentication.infra.InfraRoot;
import sopt.makers.authentication.infra.client.openfeign.config.ClientProperty;
import sopt.makers.authentication.support.SupportRoot;
import sopt.makers.authentication.support.system.security.config.SecurityProperty;


@SpringBootApplication(
		scanBasePackageClasses = {DomainRoot.class, SupportRoot.class, InfraRoot.class}
)
@EnableFeignClients
@EnableConfigurationProperties({ClientProperty.class, SecurityProperty.class})
public class AppApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
	}
}
