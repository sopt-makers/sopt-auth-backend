package sopt.makers.authentication.support.jwt;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public interface RSAKeyManager {

  RSAPublicKey getPublicKey();

  RSAPrivateKey getPrivateKey();
}
