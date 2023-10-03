package rafa.ecommerce.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import rafa.ecommerce.domain.enums.AuthProvider;

import java.util.ArrayList;
import java.util.List;

@Table(name = "users", uniqueConstraints = { @UniqueConstraint(columnNames = "user_name"),
        @UniqueConstraint(columnNames = "email") })
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class User extends BaseEntity{

    @Column(name = "user_name")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(nullable = false)
    private Boolean emailVerified = false;

    @Column(name = "provider_id")
    private String providerId;

    @Column(name = "failed_login_attempts")
    private int failedLoginAttempts;

    @Column(name = "account_non_locked")
    private boolean accountNonLocked = true;

    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.provider = AuthProvider.LOCAL;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles = new ArrayList<>();

    public boolean getAccountNonLocked() {
        return accountNonLocked;
    }
}
