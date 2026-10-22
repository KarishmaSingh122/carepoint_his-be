package com.suma.carepoint.security.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class AuthCookieService {

    private final String accessCookieName;
    private final String refreshCookieName;
    private final String accessPath;
    private final String refreshPath;
    private final String sameSite;
    private final boolean secure;

    public AuthCookieService(
            @Value("${security.cookie.access-name}")
            String accessCookieName,
            @Value("${security.cookie.refresh-name}")
            String refreshCookieName,
            @Value("${security.cookie.access-path}")
            String accessPath,
            @Value("${security.cookie.refresh-path}")
            String refreshPath,
            @Value("${security.cookie.same-site}")
            String sameSite,
            @Value("${security.cookie.secure}")
            boolean secure) {

        if (accessCookieName == null || accessCookieName.isBlank()
                || refreshCookieName == null || refreshCookieName.isBlank()) {
            throw new IllegalStateException("Cookie names must not be blank");
        }

        if (!"Strict".equalsIgnoreCase(sameSite)
                && !"Lax".equalsIgnoreCase(sameSite)
                && !"None".equalsIgnoreCase(sameSite)) {
            throw new IllegalStateException("Invalid SameSite configuration");
        }

        if ("None".equalsIgnoreCase(sameSite) && !secure) {
            throw new IllegalStateException("SameSite=None requires Secure cookies");
        }

        if (!secure && (accessCookieName.startsWith("__Secure-")
                || accessCookieName.startsWith("__Host-")
                || refreshCookieName.startsWith("__Secure-")
                || refreshCookieName.startsWith("__Host-"))) {
            throw new IllegalStateException("Prefixed secure cookies require HTTPS");
        }

        this.accessCookieName = accessCookieName;
        this.refreshCookieName = refreshCookieName;
        this.accessPath = accessPath;
        this.refreshPath = refreshPath;
        this.sameSite = sameSite;
        this.secure = secure;
    }

    public void writeTokens(HttpServletResponse response, String accessToken,
                            Duration accessTtl, String refreshToken, Duration refreshTtl) {

        addCookie(response, accessCookieName, accessToken, accessPath, accessTtl);
        addCookie(response, refreshCookieName, refreshToken, refreshPath, refreshTtl);
    }

    public void clearTokens(HttpServletResponse response) {
        addCookie(response, accessCookieName, "", accessPath, Duration.ZERO);
        addCookie(response, refreshCookieName, "", refreshPath, Duration.ZERO);
    }

    public String readRefreshToken(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return null;
        }

        for (var cookie : request.getCookies()) {
            if (refreshCookieName.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    public String accessCookieName() {
        return accessCookieName;
    }

    private void addCookie(HttpServletResponse response, String name,
                           String value, String path, Duration maxAge) {

        ResponseCookie cookie = ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(secure)
                .sameSite(sameSite)
                .path(path)
                .maxAge(maxAge)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
