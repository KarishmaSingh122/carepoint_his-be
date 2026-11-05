package com.suma.carepoint.controllers.onehcx;

import com.suma.carepoint.models.ApiResponse;
import com.suma.carepoint.services.onehcx.auth.OneHcxAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.suma.carepoint.models.constants.ApiConstant.OneHcxAuth.*;

@RestController
@RequestMapping(BASE)
@RequiredArgsConstructor
public class OneHcxAuthController {

    private final OneHcxAuthService authService;

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping(REGISTER_ACCOUNT)
    public ResponseEntity<ApiResponse> configureAccount(@RequestBody ConfigureAccountRequest request) {

        authService.configureAccount(request.username(), request.password(), request.staffId());
        return ResponseEntity.ok(new ApiResponse(1,"OneHcx account configured"));
    }

    @PostMapping(PROFILE)
    public ResponseEntity<ApiResponse> getOnehcxUser(@PathVariable Long userId) {
        return ResponseEntity.ok(authService.getOnehcxUser(userId));
    }

    public record ConfigureAccountRequest(String username, String password, Long staffId) {}
}
