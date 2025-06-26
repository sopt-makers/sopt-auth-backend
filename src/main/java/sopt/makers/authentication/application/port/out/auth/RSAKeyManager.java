package sopt.makers.authentication.application.port.out.auth;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public interface RSAKeyManager {

  RSAPublicKey getPublicKey();

  RSAPrivateKey getPrivateKey();
}
