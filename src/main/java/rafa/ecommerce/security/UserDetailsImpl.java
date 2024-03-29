package rafa.ecommerce.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import rafa.ecommerce.domain.User;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {

	private static final long serialVersionUID = 1L;

	private int id;

	private String username;

	private String email;

	private Map<String, Object> attributes;

	public static boolean isEnabled;

	private boolean accountNonLocked;

	private int failedLoginAttempts;

	@JsonIgnore
	private String password;

	private Collection<? extends GrantedAuthority> authorities;

	public UserDetailsImpl(int id, String username, String email, String password, List<GrantedAuthority> authorities, boolean accountNonLocked, int failedLoginAttempts) {

		this.id = id;
		this.username = username;
		this.email = email;
		this.password = password;
		this.authorities = authorities;
		this.accountNonLocked=accountNonLocked;
		this.failedLoginAttempts = failedLoginAttempts;
	}

	public static UserDetailsImpl build(User user) {
		List<GrantedAuthority> authorities = user.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority(role.getName().name())).collect(Collectors.toList());

		isEnabled = user.getEmailVerified();

		return new UserDetailsImpl(user.getId(), user.getUsername(), user.getEmail(), user.getPassword(),
				authorities,user.getAccountNonLocked(),user.getFailedLoginAttempts());

	}

	public int getFailedLoginAttempts() {
		return failedLoginAttempts;
	}

	public void incrementFailedLoginAttempts() {
		failedLoginAttempts++;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return isEnabled;
	}

}
