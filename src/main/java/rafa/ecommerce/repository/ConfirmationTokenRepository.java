package rafa.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rafa.ecommerce.domain.EmailConfirmationToken;

import java.util.Optional;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<EmailConfirmationToken, Integer> {

	Optional<EmailConfirmationToken> findByConfirmationToken(String confirmationToken);
	
	
}
