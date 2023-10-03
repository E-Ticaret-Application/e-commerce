package rafa.ecommerce.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class AuthenticationEvents {

	//Authentication başarılı olduğunda veya başarısız olduğunda çalışacak iki methodu barındırı
	
	private final UserDetailsServiceImpl userDetailsServiceImpl;
	
	@EventListener
    public void onSuccess(AuthenticationSuccessEvent success) {
		
		
    }

    @EventListener
    public void onFailure(AbstractAuthenticationFailureEvent failures) {
        String email = (String) failures.getAuthentication().getPrincipal();
        userDetailsServiceImpl.incrementFailedLoginAttempts(email);
    }
}
