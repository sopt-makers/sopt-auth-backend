package sopt.makers.authentication.infra.client.openfeign.config;


import feign.Logger;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@RequiredArgsConstructor
@EnableFeignClients
public class OpenFeignConfig {

    // Log
    @Bean
    @Profile({"local", "test"})
    feign.Logger.Level feignLogLevelOnDevAndTest() {
        return Logger.Level.FULL;
    }

    @Bean
    @Profile({"prod", "dev"})
    feign.Logger.Level feignLogLevelOnProd(){
        return Logger.Level.NONE;
    }

}
