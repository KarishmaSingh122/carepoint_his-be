package com.suma.carepoint.models.auth;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {
    private String username;
    /** Role names without the ROLE_ prefix, e.g. DOCTOR. */
    private List<String> roles;
    /** Permission names, e.g. PATIENT_READ. */
    private List<String> permissions;
}
