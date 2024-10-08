package sopt.makers.authentication.support.system.security.config;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("security")
public record SecurityProperty(
        Auth auth
) {

    public record Auth(
            Url url
    ) {
        public record Url(
                String local,
                String dev,
                String prod
        ) {
        }

    }


}
