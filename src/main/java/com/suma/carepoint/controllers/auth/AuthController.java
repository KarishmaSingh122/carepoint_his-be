package com.suma.carepoint.controllers.auth;

import com.suma.carepoint.models.auth.LoginRequest;
import com.suma.carepoint.services.auth.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.suma.carepoint.models.constants.ApiConstant.Auth.*;

@RequestMapping(BASE)
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(LOGIN)
    public ResponseEntity<AuthService.AuthResult> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response) {

        return ResponseEntity.ok()
                .cacheControl(CacheControl.noStore())
                .body(authService.login(request, response));
    }

    @PostMapping(REFRESH)
    public ResponseEntity<AuthService.AuthResult> refresh(
            HttpServletRequest request,
            HttpServletResponse response) {

        return ResponseEntity.ok()
                .cacheControl(CacheControl.noStore())
                .body(authService.refresh(request, response));
    }

    @PostMapping(LOGOUT)
    public ResponseEntity<AuthService.AuthResult> logout(
            HttpServletRequest request,
            HttpServletResponse response) {

        return ResponseEntity.ok()
                .cacheControl(CacheControl.noStore())
                .body(authService.logout(request, response));
    }
}

