package com.suma.carepoint.models.auth;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RolePermissionResponse {

    private Long roleId;
    private String roleName;
    private Long permissionId;
    private String permissionName;
    private String moduleName;
    private String action;
}