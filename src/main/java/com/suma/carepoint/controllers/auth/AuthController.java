package com.suma.carepoint.controllers.auth;

import com.suma.carepoint.models.auth.LoginRequest;
import com.suma.carepoint.models.auth.LoginResponse;
import com.suma.carepoint.models.constants.ApiConstant;
import com.suma.carepoint.services.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping(ApiConstant.Auth.BASE)
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(ApiConstant.Auth.LOGIN)
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {

        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}

