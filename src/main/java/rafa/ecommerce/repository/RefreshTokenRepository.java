package rafa.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rafa.ecommerce.domain.RefreshToken;
import rafa.ecommerce.domain.User;

import java.util.Optional;


public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer>{

		Optional<RefreshToken> findByToken(String token);

		int deleteByUser(User user);
}
