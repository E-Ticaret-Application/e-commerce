package rafa.ecommerce.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rafa.ecommerce.config.AppProperties;
import rafa.ecommerce.domain.RefreshToken;
import rafa.ecommerce.exception.TokenRefreshException;
import rafa.ecommerce.repository.RefreshTokenRepository;
import rafa.ecommerce.repository.UserRepository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final AppProperties appProperties;

    private final RefreshTokenRepository refreshTokenRepository;

    private final UserRepository userRepository;


    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Transactional
    public RefreshToken createRefreshToken(int id) {

        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(userRepository.findById(id).get());
        refreshToken.setExpiryDate(Instant.now().plusMillis(appProperties.getAuth().getJwtRefreshExpirationMs()));
        refreshToken.setToken(UUID.randomUUID().toString());
        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token) throws TokenRefreshException {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(),
                    "Refresh token was expired. please make a new signin request");

        }
        return token;
    }

    @Transactional
    public int deleteByUserId(int userId) {
        return refreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
    }
}
