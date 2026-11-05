
package com.suma.carepoint.security.jwt;

import com.suma.carepoint.entities.auth.RefreshToken;
import com.suma.carepoint.repositories.auth.RefreshTokenRepository;
import com.suma.carepoint.security.UserPrincipal;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.HexFormat;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.suma.carepoint.models.constants.ApiConstant.Security.REFRESH_HASH_ALGORITHM;

@Service
@RequiredArgsConstructor
public class TokenService {

    private static final SecureRandom RANDOM = new SecureRandom();

    private final JwtEncoder jwtEncoder;
    private final RefreshTokenRepository repository;
    private final UserDetailsService userDetailsService;
    private final PlatformTransactionManager transactionManager;

    @Value("${security.jwt.issuer}")
    private String issuer;

    @Value("${security.jwt.audience}")
    private String audience;

    @Value("${security.jwt.access-token-ttl}")
    private Duration accessTokenTtl;

    @Value("${security.jwt.refresh-token-ttl:P7D}")
    private Duration refreshTokenTtl;

    @PostConstruct
    void validateConfiguration() {
        if (accessTokenTtl == null || accessTokenTtl.isZero()
                || accessTokenTtl.isNegative()) {
            throw new IllegalStateException("Access token TTL must be positive");
        }

        if (refreshTokenTtl == null || refreshTokenTtl.isZero()
                || refreshTokenTtl.isNegative()) {
            throw new IllegalStateException("Refresh token TTL must be positive");
        }
    }

    public String issueAccessToken(UserPrincipal principal) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(issuer)
                .subject(principal.getUsername())
                .audience(List.of(audience))
                .issuedAt(now)
                .expiresAt(now.plus(accessTokenTtl))
                .claim("uid", principal.userId())
                .claim("sid", principal.staffId())
                .build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();

        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }

    public IssuedRefreshToken createRefreshToken(UserPrincipal principal) {
        Instant now = Instant.now();
        String rawToken = generateToken();

        RefreshToken token = new RefreshToken();
        token.setUserId(principal.userId());
        token.setUsername(principal.getUsername());
        token.setTokenHash(hash(rawToken));
        token.setFamilyId(UUID.randomUUID());
        token.setCreatedAt(now);
        token.setExpiresAt(now.plus(refreshTokenTtl));

        repository.save(token);

        return new IssuedRefreshToken(rawToken, token.getExpiresAt());
    }

    public RotatedRefreshToken rotateRefreshToken(String rawToken) {
        if (rawToken == null || rawToken.isBlank()) {
            throw invalidRefreshToken();
        }

        TransactionTemplate transaction = new TransactionTemplate(transactionManager);

        RotationOutcome outcome = transaction.execute(status -> {
            RefreshToken current = repository
                    .findByTokenHashForUpdate(hash(rawToken))
                    .orElse(null);

            if (current == null) {
                return RotationOutcome.invalid();
            }

            Instant now = Instant.now();

            if (current.getRevokedAt() != null) {
                revokeFamily(current.getFamilyId(), now);
                return RotationOutcome.replay();
            }

            if (!current.getExpiresAt().isAfter(now)) {
                current.setRevokedAt(now);
                repository.save(current);
                return RotationOutcome.invalid();
            }

            UserDetails loaded;

            try {
                loaded = userDetailsService.loadUserByUsername(current.getUsername());
            } catch (UsernameNotFoundException ex) {
                current.setRevokedAt(now);
                repository.save(current);
                return RotationOutcome.invalid();
            }

            if (!(loaded instanceof UserPrincipal principal)
                    || principal.userId() == null
                    || principal.staffId() == null
                    || !Objects.equals(
                    principal.userId(), current.getUserId())
                    || !Objects.equals(
                    principal.getUsername(), current.getUsername())) {
                current.setRevokedAt(now);
                repository.save(current);
                return RotationOutcome.invalid();
            }

            String newRawToken = generateToken();
            String newHash = hash(newRawToken);

            RefreshToken replacement = new RefreshToken();
            replacement.setUserId(current.getUserId());
            replacement.setUsername(current.getUsername());
            replacement.setTokenHash(newHash);
            replacement.setFamilyId(current.getFamilyId());
            replacement.setCreatedAt(now);
            replacement.setExpiresAt(now.plus(refreshTokenTtl));

            current.setRevokedAt(now);
            current.setReplacedByHash(newHash);

            repository.save(current);
            repository.save(replacement);

            return RotationOutcome.success(principal, newRawToken, replacement.getExpiresAt());
        });

        if (outcome == null
                || outcome.status() == RotationStatus.INVALID) {
            throw invalidRefreshToken();
        }

        if (outcome.status() == RotationStatus.REPLAY) {
            throw new BadCredentialsException("Refresh token reuse detected; session revoked");
        }

        return new RotatedRefreshToken(outcome.principal(), outcome.rawToken(), outcome.expiresAt());
    }

    public void revokeRefreshToken(String rawToken) {
        if (rawToken == null || rawToken.isBlank()) {
            return;
        }

        TransactionTemplate transaction = new TransactionTemplate(transactionManager);

        transaction.executeWithoutResult(status ->
                repository.findByTokenHashForUpdate(hash(rawToken))
                        .ifPresent(token -> {
                            if (token.getRevokedAt() == null) {
                                token.setRevokedAt(Instant.now());
                                repository.save(token);
                            }
                        })
        );
    }

    private void revokeFamily(UUID familyId, Instant now) {
        repository.findActiveByFamilyId(familyId).forEach(token -> {
            token.setRevokedAt(now);
            repository.save(token);
        });
    }

    private String generateToken() {
        byte[] bytes = new byte[32];
        RANDOM.nextBytes(bytes);

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);
    }

    private String hash(String rawToken) {
        try {
            byte[] digest = MessageDigest.getInstance(REFRESH_HASH_ALGORITHM)
                    .digest(rawToken.getBytes(StandardCharsets.UTF_8));

            return HexFormat.of().formatHex(digest);
        } catch (Exception ex) {
            throw new IllegalStateException(REFRESH_HASH_ALGORITHM + " is unavailable", ex);
        }
    }

    private BadCredentialsException invalidRefreshToken() {
        return new BadCredentialsException("Invalid refresh token");
    }

    public Duration getAccessTokenTtl() {
        return accessTokenTtl;
    }

    public Duration getRefreshTokenTtl() {
        return refreshTokenTtl;
    }

    public record IssuedRefreshToken(String value, Instant expiresAt) {}

    public record RotatedRefreshToken(
            UserPrincipal principal, String value, Instant expiresAt) {}

    private enum RotationStatus {
        SUCCESS, INVALID, REPLAY
    }

    private record RotationOutcome(
            RotationStatus status, UserPrincipal principal, String rawToken, Instant expiresAt) {

        static RotationOutcome invalid() {
            return new RotationOutcome(RotationStatus.INVALID, null, null, null);
        }

        static RotationOutcome replay() {
            return new RotationOutcome(RotationStatus.REPLAY, null, null, null);
        }

        static RotationOutcome success(UserPrincipal principal, String rawToken, Instant expiresAt) {
            return new RotationOutcome(RotationStatus.SUCCESS, principal, rawToken, expiresAt);
        }
    }
}
