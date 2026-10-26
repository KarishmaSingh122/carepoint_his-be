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

}
