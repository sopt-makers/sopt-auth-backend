package sopt.makers.authentication.support.util.jwt;

import java.io.IOException;

public interface JwtProvider <T>{
    String generate(final T value);

    T parse(final String token) throws IOException;

}
