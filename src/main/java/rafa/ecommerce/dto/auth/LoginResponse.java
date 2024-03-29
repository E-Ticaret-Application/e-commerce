package rafa.ecommerce.dto.auth;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {



	private int id;
	private String firstName;
	private String lastName;
	private String userName;
	private String email;

//	private String password;
//	private String role;
	private String token;
	private String refreshToken;
	private Long accessTokenExpiry;

	
}
