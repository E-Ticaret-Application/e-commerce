package rafa.ecommerce.service;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rafa.ecommerce.config.AppProperties;
import rafa.ecommerce.core.ModelMapperService;
import rafa.ecommerce.domain.Customer;
import rafa.ecommerce.domain.RefreshToken;
import rafa.ecommerce.domain.Role;
import rafa.ecommerce.domain.enums.ERole;
import rafa.ecommerce.dto.auth.LoginRequest;
import rafa.ecommerce.dto.auth.LoginResponse;
import rafa.ecommerce.dto.auth.RegisterRequest;
import rafa.ecommerce.repository.CustomerRepository;
import rafa.ecommerce.repository.RoleRepository;
import rafa.ecommerce.repository.UserRepository;
import rafa.ecommerce.security.JwtUtils;
import rafa.ecommerce.security.UserDetailsImpl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@Service("authorization")
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CustomerRepository customerRepository;
    private final EmailService emailService;
    private final AppProperties appProperties;
    private final ModelMapperService modelMapperService;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, CustomerRepository customerRepository, EmailService emailService, AppProperties appProperties, ModelMapperService modelMapperService, JwtUtils jwtUtils, RefreshTokenService refreshTokenService, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.customerRepository = customerRepository;
        this.emailService = emailService;
        this.appProperties = appProperties;
        this.modelMapperService = modelMapperService;
        this.jwtUtils = jwtUtils;
        this.refreshTokenService = refreshTokenService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public String registerUser(RegisterRequest registerRequest) {

        Customer customer = new Customer(registerRequest.getFirstName(), registerRequest.getLastName(),
                registerRequest.getUserName(), passwordEncoder.encode(registerRequest.getPassword()),
                registerRequest.getEmail());

        List<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findByName(ERole.ROLE_USER).get());

        customer.setRoles(roles);
        customerRepository.save(customer);
        try {
            emailService.sendEmail(customer);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (MessagingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return "Verify email by the link sent on your email address";

    }

    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwt = jwtUtils.generateJwtToken(userDetails);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        Customer customer = customerRepository.findByEmail(loginRequest.getEmail());

        return new LoginResponse(customer.getId(), customer.getFirstName(), customer.getLastName(),
                customer.getUsername(), customer.getEmail(), jwt, refreshToken.getToken(),
                System.currentTimeMillis() + appProperties.getAuth().getJwtExpirationMs());

    }

}
