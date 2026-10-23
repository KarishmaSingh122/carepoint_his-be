package com.suma.carepoint.models.auth;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {
    private List<String> roles;
//    private String email;
}
