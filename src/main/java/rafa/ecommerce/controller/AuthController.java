package rafa.ecommerce.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rafa.ecommerce.dto.auth.LoginRequest;
import rafa.ecommerce.dto.auth.LoginResponse;
import rafa.ecommerce.dto.auth.RegisterRequest;
import rafa.ecommerce.service.AuthService;
import rafa.ecommerce.service.EmailService;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final EmailService emailService;

    @Value("${app.email_verifed_callbackurl}")
    private String emailVerifedCallbackUrl;

    public AuthController(AuthService authService, EmailService emailService) {
        this.authService = authService;
        this.emailService = emailService;
    }


    @PostMapping("/register")
    public String register(@Valid @RequestBody RegisterRequest registerRequest) {
        return authService.registerUser(registerRequest);
    }

    @RequestMapping(value = "/confirm-account", method = { RequestMethod.GET, RequestMethod.POST })
    public ResponseEntity<String> confirmUserAccount(@RequestParam("token") String confirmationToken) {
        emailService.confirmEmail(confirmationToken);

        return ResponseEntity.status(HttpStatus.TEMPORARY_REDIRECT)
                .location(URI.create(emailVerifedCallbackUrl + "/auth/login")).build();

    }


    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }
}
