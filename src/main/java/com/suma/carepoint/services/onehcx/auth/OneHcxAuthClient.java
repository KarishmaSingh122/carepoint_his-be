package com.suma.carepoint.services.onehcx.auth;

import com.suma.carepoint.models.ApiResponse;
import com.suma.carepoint.models.auth.LoginRequest;
import com.suma.carepoint.models.onehcx.OneHcxLoginResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import static com.suma.carepoint.models.constants.OneHcxClientConstant.Auth.*;

@FeignClient(
        name = "oneHcxAuthClient",
        url = "${onehcx.auth.base-url}" + BASE,
        configuration = FeignAuthConfig.class
)
public interface OneHcxAuthClient {

//    //    @RequestHeader("Authorization") String token
    @PostMapping(LOGIN)
    OneHcxLoginResponse login(LoginRequest request);

    @PostMapping(LOGOUT)
    ApiResponse logout();

    @GetMapping(USER_DETAILS)
    ApiResponse getUserDetailsByToken(@RequestHeader("X-User-ID") Long userId);
}


/**
 * spring:
 *   cloud:
 *     openfeign:
 *       client:
 *         config:
 *           userClient:
 *             connectTimeout: 3000
 *             readTimeout: 5000
 *           paymentClient:
 *             connectTimeout: 3000
 *             readTimeout: 10000
 *           notificationClient:
 *             connectTimeout: 3000
 *             readTimeout: 5000
 */
