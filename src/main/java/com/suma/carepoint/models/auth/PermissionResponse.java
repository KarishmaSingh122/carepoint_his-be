package com.suma.carepoint.models.auth;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PermissionResponse {

    private Long permissionId;
    private String permissionName;
    private String moduleName;
    private String action;
    private String description;
}
