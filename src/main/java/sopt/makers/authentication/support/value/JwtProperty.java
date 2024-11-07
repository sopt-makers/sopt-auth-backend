package sopt.makers.authentication.support.value;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "rsa")
public record JwtProperty(RSAPublicKey publicKey, RSAPrivateKey privateKey) {}
