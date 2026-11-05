package com.suma.carepoint.security.jwt;

import com.suma.carepoint.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class CarePointJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final UserDetailsService userDetailsService;

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {

        String username = jwt.getSubject();

        if (username == null || username.isBlank()) {
            throw invalidToken();
        }

        Long tokenUserId = readLongClaim(jwt, "uid");
        Long tokenStaffId = readLongClaim(jwt, "sid");

        UserDetails loaded = userDetailsService.loadUserByUsername(username);

        if (!(loaded instanceof UserPrincipal principal)) {
            throw invalidToken();
        }

        if (principal.userId() == null
                || principal.staffId() == null
                || !Objects.equals(principal.userId(), tokenUserId)
                || !Objects.equals(principal.staffId(), tokenStaffId)
                || !Objects.equals(principal.getUsername(), username)) {
            throw invalidToken();
        }

        return UsernamePasswordAuthenticationToken.authenticated(
                principal,
                null,
                principal.getAuthorities()
        );
    }

    private Long readLongClaim(Jwt jwt, String name) {

        Object value = jwt.getClaim(name);

        if (!(value instanceof Number number)) {
            throw invalidToken();
        }

        long result = number.longValue();

        // JWT identity claims must be positive database identifiers.
        if (result <= 0) {
            throw invalidToken();
        }

        return result;
    }

    private BadCredentialsException invalidToken() {
        return new BadCredentialsException("Invalid access token");
    }
}