package com.melilla.gestPlanes.service.impl;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.melilla.gestPlanes.exceptions.exceptions.TokenRefreshException;
import com.melilla.gestPlanes.model.RefreshToken;
import com.melilla.gestPlanes.model.User;
import com.melilla.gestPlanes.repository.RefreshTokenRepository;
import com.melilla.gestPlanes.repository.UserRepository;
import com.melilla.gestPlanes.service.RefreshTokenService;

import jakarta.transaction.Transactional;
import lombok.extern.java.Log;
@Log
@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
  @Value("${app.jwtRefreshExpirationMs}")
  private Long refreshTokenDurationMs;

  @Autowired
  private RefreshTokenRepository refreshTokenRepository;

  @Autowired
  private UserRepository userRepository;

  public Optional<RefreshToken> findByToken(String token) {
	log.info("refreshService: "+token);  
    return refreshTokenRepository.findByToken(token);
  }

  @Transactional
  public RefreshToken createRefreshToken(Long userId) {
    RefreshToken refreshToken = new RefreshToken();
    
    User usuario =userRepository.findById(userId).get();

    refreshTokenRepository.deleteAllByUser(usuario);
    refreshToken.setUser(usuario);
    refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
    refreshToken.setToken(UUID.randomUUID().toString());

    refreshToken = refreshTokenRepository.save(refreshToken);
    return refreshToken;
  }

  public RefreshToken verifyExpiration(RefreshToken token) {
    if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
      refreshTokenRepository.delete(token);
      throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
    }

    return token;
  }

  @Transactional
  public int deleteByUserId(Long userId) {
    return refreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
  }
}
