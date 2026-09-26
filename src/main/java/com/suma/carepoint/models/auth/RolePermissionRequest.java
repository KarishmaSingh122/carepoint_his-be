package com.suma.carepoint.models.auth;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RolePermissionRequest {

    @NotNull
    @Positive
    private Long roleId;

    @NotNull
    @Positive
    private Long permissionId;
}
