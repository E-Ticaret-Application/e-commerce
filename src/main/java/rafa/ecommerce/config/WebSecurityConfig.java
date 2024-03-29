package rafa.ecommerce.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import rafa.ecommerce.security.AuthenticationEntryPointJwt;
import rafa.ecommerce.security.AuthenticationTokenFilter;
import rafa.ecommerce.security.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class WebSecurityConfig {

	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Autowired
	private AuthenticationEntryPointJwt unauthorizedHandler;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Bean
	public AuthenticationTokenFilter authenticationJwtTokenFilter() {
		return new AuthenticationTokenFilter();
	}

	@Bean
	public AuthenticationManager authManager(HttpSecurity http) throws Exception {
		return http.getSharedObject(AuthenticationManagerBuilder.class).userDetailsService(userDetailsService)
				.passwordEncoder(passwordEncoder).and().build();
	}

	@Bean
	public AuthenticationEventPublisher authenticationEventPublisher(
			ApplicationEventPublisher applicationEventPublisher) {
		return new DefaultAuthenticationEventPublisher(applicationEventPublisher);
	}

	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.addAllowedOrigin("*"); // Tüm originlere izin vermek için "*" 
		config.addAllowedMethod("*"); // Tüm HTTP metotlarına izin vermek için "*"
		config.addAllowedHeader("*"); // Tüm headerlara izin vermek için "*" 
		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}

	@Bean()
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf().disable().exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				.authorizeHttpRequests((request) -> request
						.requestMatchers("/api/v1/products/**", "/api/v1/brands/**", "/api/v1/cities/**",
								"/api/v1/email/**", "/api/v1/countries/**", "/api/v1/coupons/**",
								"/api/v1/districts/**","/api/v1/paymnets/retrieveCheckoutRequest",
								"/api/v1/auth/confirm-account"
								)
						.permitAll().requestMatchers("/api/v1/categories/**").permitAll()
						.requestMatchers("/v3/api-docs/**", "/swagger-ui/**").permitAll()
						.requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll()
						.requestMatchers("/api/v1/auth/login").permitAll().requestMatchers("/api/v1/auth/register")
						.permitAll().requestMatchers("/api/v1/auth/refresh-token").permitAll().anyRequest()
						.authenticated());

		http.headers().frameOptions().disable();

		http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
		http.addFilterBefore(corsFilter(), ChannelProcessingFilter.class);
		return http.build();

	}

}
