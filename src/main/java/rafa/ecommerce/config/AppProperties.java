package rafa.ecommerce.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public class AppProperties {

	private final Auth auth = new Auth();
	//private final Iyzico iyzico = new Iyzico();


	@Data
	public static class Auth {
		private String jwtSecret;
		private Long jwtExpirationMs;
		private Long jwtRefreshExpirationMs;
	}

	
/*	@Data
	public static class Iyzico {
		private String apiKey;
		private String secretKey;
		private String baseUrl;
	}*/



	/*public Iyzico getIyzico() {
		return iyzico;
	}*/

	public Auth getAuth() {
		return auth;
	}
}
