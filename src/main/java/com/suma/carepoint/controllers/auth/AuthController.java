package com.suma.carepoint.controllers.auth;
import com.suma.carepoint.models.ApiResponse;
import com.suma.carepoint.models.auth.LoginRequest;
import com.suma.carepoint.services.auth.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.suma.carepoint.security.UserPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import static com.suma.carepoint.models.constants.ApiConstant.Auth.*;

@RequestMapping(BASE)
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(LOGIN)
    public ResponseEntity<ApiResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response) {

        return ResponseEntity.ok()
                .cacheControl(CacheControl.noStore())
                .body(new ApiResponse(
                        1,
                        "Login successful.",
                        authService.login(request, response))
                );
    }

    @PostMapping(REFRESH)
    public ResponseEntity<ApiResponse> refresh(
            HttpServletRequest request,
            HttpServletResponse response) {

        return ResponseEntity.ok()
                .cacheControl(CacheControl.noStore())
                .body(new ApiResponse(
                        1,
                        "Token refresh.",
                        authService.refresh(request, response))
                );
    }

    @PostMapping(LOGOUT)
    public ResponseEntity<ApiResponse> logout(
            HttpServletRequest request,
            HttpServletResponse response) {

        return ResponseEntity.ok()
                .cacheControl(CacheControl.noStore())
                .body(new ApiResponse(
                        1,
                        "Logout successful.",
                        authService.logout(request, response))
                );
    }

    @GetMapping(ME)
    public ResponseEntity<ApiResponse> me(@AuthenticationPrincipal UserPrincipal principal) {

        return ResponseEntity.ok()
                .cacheControl(CacheControl.noStore())
                .body(new ApiResponse(1, "", authService.me(principal)));
    }
}

