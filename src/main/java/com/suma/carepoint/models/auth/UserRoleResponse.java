package com.suma.carepoint.models.auth;


import lombok.*;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRoleResponse {

    private Long userId;
    private String username;
    private Long roleId;
    private String roleName;
    private Instant assignedAt;
}
