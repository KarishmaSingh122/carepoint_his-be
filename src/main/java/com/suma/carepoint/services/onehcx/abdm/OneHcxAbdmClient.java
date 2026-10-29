package com.suma.carepoint.services.onehcx.abdm;
import com.suma.carepoint.models.onehcx.OneHcxGenerateOTPResponse;
import com.suma.carepoint.services.onehcx.auth.FeignAuthConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Map;
import static com.suma.carepoint.models.constants.OneHcxClientConstant.Abdm.*;


@FeignClient(
        name = "oneHcxAbdmClient",
        url = "${onehcx.abha.base-url}" + BASE,
        configuration = FeignAuthConfig.class
)

public interface OneHcxAbdmClient {

    @PostMapping(GENERATE_OTP)
    OneHcxGenerateOTPResponse generateOtp(@RequestBody Map<String, String> request);


    @PostMapping(VERIFY_OTP)
    Map<String, Object> verifyOtp(@RequestBody Map<String, String> request);
}

