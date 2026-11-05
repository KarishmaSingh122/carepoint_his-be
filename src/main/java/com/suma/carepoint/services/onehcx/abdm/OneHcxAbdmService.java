package com.suma.carepoint.services.onehcx.abdm;
import com.suma.carepoint.models.onehcx.OneHcxGenerateOTPResponse;
import com.suma.carepoint.services.onehcx.auth.OneHcxAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OneHcxAbdmService {

    private final OneHcxAbdmClient oneHcxAbhaClient;
    private final OneHcxAuthService oneHcxAuthService;

    public OneHcxGenerateOTPResponse initiateVerification(String idNumber) {
        oneHcxAuthService.getValidToken();
        OneHcxGenerateOTPResponse response =  oneHcxAbhaClient.generateOtp(
                Map.of(
                        "idNumber", idNumber,
                        "type", "ABHA_NUMBER"
                )
        );

        return response;
    }

    public Map<String, Object> verifyOtp(String otp, String txnId) {
        oneHcxAuthService.getValidToken();
        return oneHcxAbhaClient.verifyOtp(
                Map.of("otp", otp, "txnId", txnId, "type", "ABHA_NUMBER")
        );
    }

}

