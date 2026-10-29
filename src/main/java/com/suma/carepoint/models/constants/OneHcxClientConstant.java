package com.suma.carepoint.models.constants;

import lombok.NoArgsConstructor;


@NoArgsConstructor
public class OneHcxClientConstant {


    public static class Auth {
        public static final String BASE = "/service/auth/api/v1";
        public static final String LOGIN = "/hms/user-login";
        public static final String LOGOUT="/hms/user-logout";
        public static final String USER_DETAILS="/user-details";
    }


    public static class Abdm {
        public static final String BASE = "/service/abdm/v3/hmis/healthId/profile/login";
        public static final String GENERATE_OTP = "/hmis-generateOtp";
        public static final String VERIFY_OTP = "/hmis-validateOtp";
    }
}
