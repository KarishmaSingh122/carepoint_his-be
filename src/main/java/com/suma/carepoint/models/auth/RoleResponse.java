package com.suma.carepoint.models.auth;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleResponse {

    private Long roleId;
    private String roleName;
    private String description;
    private boolean active;
}
