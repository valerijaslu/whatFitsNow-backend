package com.whatfitsnow.whatfitsnowbackend.auth;

import com.whatfitsnow.whatfitsnowbackend.auth.api.AuthResponse;
import com.whatfitsnow.whatfitsnowbackend.auth.api.LoginRequest;
import com.whatfitsnow.whatfitsnowbackend.auth.api.RegisterRequest;
import com.whatfitsnow.whatfitsnowbackend.common.exception.ConflictException;
import com.whatfitsnow.whatfitsnowbackend.common.exception.UnauthorizedException;
import com.whatfitsnow.whatfitsnowbackend.security.JwtService;
import com.whatfitsnow.whatfitsnowbackend.user.User;
import com.whatfitsnow.whatfitsnowbackend.user.UserRepository;
import com.whatfitsnow.whatfitsnowbackend.user.vo.PasswordHash;
import com.whatfitsnow.whatfitsnowbackend.user.vo.UserEmail;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;

  public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
  }

  @Transactional
  public AuthResponse register(RegisterRequest req) {
    UserEmail email = UserEmail.of(req.email());

    if (userRepository.existsByEmail(email)) {
      throw new ConflictException("Email already registered");
    }

    String encoded = passwordEncoder.encode(req.password());
    User user = User.builder()
        .email(email)
        .passwordHash(PasswordHash.of(encoded))
        .build();
    User saved = userRepository.save(user);

    String token = jwtService.createAccessToken(saved.getId(), saved.getEmail());
    return AuthResponse.bearer(token);
  }

  @Transactional(readOnly = true)
  public AuthResponse login(LoginRequest req) {
    UserEmail email = UserEmail.of(req.email());

    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

    if (!passwordEncoder.matches(req.password(), user.getPasswordHash())) {
      throw new UnauthorizedException("Invalid email or password");
    }

    String token = jwtService.createAccessToken(user.getId(), user.getEmail());
    return AuthResponse.bearer(token);
  }
}

