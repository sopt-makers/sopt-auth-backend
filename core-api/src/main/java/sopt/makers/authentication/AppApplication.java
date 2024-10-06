package sopt.makers.authentication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import sopt.makers.authentication.infra.InfraRoot;
import sopt.makers.authentication.support.SupportRoot;

@SpringBootApplication(
		scanBasePackageClasses = {DomainRoot.class, SupportRoot.class, InfraRoot.class}
)
public class AppApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
	}
}
