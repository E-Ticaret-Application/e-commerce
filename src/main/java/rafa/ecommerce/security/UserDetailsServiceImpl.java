package rafa.ecommerce.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import rafa.ecommerce.domain.User;
import rafa.ecommerce.repository.UserRepository;


@Component
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserRepository userRepository;

	public UserDetailsServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Email Not Found with email: " + email));

		return UserDetailsImpl.build(user);
	}
	
	
	  public void incrementFailedLoginAttempts(String email) {
		  User user = userRepository.findByEmail(email)
					.orElseThrow(() -> new UsernameNotFoundException("Email Not Found with email: " + email));
		  
		  
		  user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
		  if(user.getFailedLoginAttempts() >= 5) {
			  user.setAccountNonLocked(false);
		  }
		  
		  userRepository.save(user);
		  
	    }

}
