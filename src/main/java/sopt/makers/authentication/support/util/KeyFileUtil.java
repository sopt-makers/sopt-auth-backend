package sopt.makers.authentication.support.util;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.util.Optional;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.core.io.ClassPathResource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class KeyFileUtil {
  private static final Charset UTF_8 = StandardCharsets.UTF_8;

  public static Optional<PrivateKey> getPrivateKey(String keyPath) {
    try (PEMParser pemParser = createPemParser(keyPath)) {
      return parsePrivateKey(pemParser);
    } catch (IOException e) {
      log.error(e.getMessage());
      return Optional.empty();
    }
  }

  private static PEMParser createPemParser(String appleKeyPath) throws IOException {
    ClassPathResource resource = new ClassPathResource(appleKeyPath);
    String privateKey = new String(resource.getInputStream().readAllBytes(), UTF_8);
    return new PEMParser(new StringReader(privateKey));
  }

  private static Optional<PrivateKey> parsePrivateKey(PEMParser pemParser) throws IOException {
    JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
    PrivateKeyInfo privateKeyInfo = (PrivateKeyInfo) pemParser.readObject();
    return Optional.of(converter.getPrivateKey(privateKeyInfo));
  }
}
