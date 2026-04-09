package com.whatfitsnow.whatfitsnowbackend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

  private final JwtProperties props;
  private final SecretKey key;

  public JwtService(JwtProperties props) {
    this.props = props;
    this.key = Keys.hmacShaKeyFor(props.secret().getBytes(StandardCharsets.UTF_8));
  }

  public String createAccessToken(long userId, String email) {
    Instant now = Instant.now();
    Instant exp = now.plus(props.accessTokenTtl());

    return Jwts.builder()
        .issuer(props.issuer())
        .subject(Long.toString(userId))
        .issuedAt(Date.from(now))
        .expiration(Date.from(exp))
        .claim("email", email)
        .signWith(key)
        .compact();
  }

  public Claims parse(String token) {
    return Jwts.parser()
        .verifyWith(key)
        .requireIssuer(props.issuer())
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }
}

