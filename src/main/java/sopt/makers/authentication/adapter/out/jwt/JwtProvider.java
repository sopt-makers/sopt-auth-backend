package sopt.makers.authentication.adapter.out.jwt;

import java.io.IOException;

public interface JwtProvider<T> {

  String generateJwt(final T value);

  T parse(final String token) throws IOException;
}
