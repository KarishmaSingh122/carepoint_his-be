package com.suma.carepoint.models.onehcx;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OneHcxGenerateOTPResponse {
    private String txnId;
    private String message;
    private String code;
    private String loginId;
    private String scope;
    private String loginHint;
    private String timeStamp;
    private String error;
    private String errors;
    private String httpStatus;
}
