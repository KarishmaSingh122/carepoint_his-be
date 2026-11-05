package com.suma.carepoint.entities.onehcx;

import com.suma.carepoint.entities.organization.Staff;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "onehcx_account",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_onehcx_account_username",
                        columnNames = "username")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class OneHcxAccount{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, length = 150)
    private String username;

    @Column(name = "password", columnDefinition = "TEXT")
    private String password;

    @Column(name = "token_type", length = 30)
    private String tokenType;

    @Column(name = "token_value", columnDefinition = "TEXT")
    private String tokenValue;

    @Column(name = "token_expiry")
    private LocalDateTime tokenExpiry;

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_status", nullable = false, length = 20)
    @Builder.Default
    private OneHcxAuthStatus authStatus = OneHcxAuthStatus.LOGGED_OUT;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Column(name = "last_password_change")
    private LocalDateTime lastPasswordChange;

    @Column(name = "last_token_refresh")
    private LocalDateTime lastTokenRefresh;

    @Column(name = "last_error", length = 1000)
    private String lastError;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_id")
    private Staff staff;
}
