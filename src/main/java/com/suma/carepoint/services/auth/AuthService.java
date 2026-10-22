
package com.suma.carepoint.services.auth;

import com.suma.carepoint.models.auth.LoginRequest;
import com.suma.carepoint.security.UserPrincipal;
import com.suma.carepoint.security.jwt.AuthCookieService;
import com.suma.carepoint.security.jwt.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final AuthCookieService authCookieService;

    public AuthResult login(
            LoginRequest request,
            HttpServletResponse response) {

        Authentication authentication =
                authenticationManager.authenticate(
                        UsernamePasswordAuthenticationToken.unauthenticated(
                                request.username(),
                                request.password()
                        )
                );

        UserPrincipal principal =
                (UserPrincipal) authentication.getPrincipal();

        String accessToken =
                tokenService.issueAccessToken(principal);

        TokenService.IssuedRefreshToken refresh =
                tokenService.createRefreshToken(principal);

        authCookieService.writeTokens(
                response,
                accessToken,
                tokenService.getAccessTokenTtl(),
                refresh.value(),
                tokenService.getRefreshTokenTtl()
        );

        return new AuthResult(true);
    }

    public AuthResult refresh(
            HttpServletRequest request,
            HttpServletResponse response) {

        String rawRefreshToken =
                authCookieService.readRefreshToken(request);

        TokenService.RotatedRefreshToken rotated =
                tokenService.rotateRefreshToken(rawRefreshToken);

        String accessToken =
                tokenService.issueAccessToken(rotated.principal());

        authCookieService.writeTokens(
                response,
                accessToken,
                tokenService.getAccessTokenTtl(),
                rotated.value(),
                tokenService.getRefreshTokenTtl()
        );

        return new AuthResult(true);
    }

    public AuthResult logout(
            HttpServletRequest request,
            HttpServletResponse response) {

        String rawRefreshToken =
                authCookieService.readRefreshToken(request);

        tokenService.revokeRefreshToken(rawRefreshToken);
        authCookieService.clearTokens(response);

        return new AuthResult(true);
    }

    public record AuthResult(boolean authenticated) {}
}
