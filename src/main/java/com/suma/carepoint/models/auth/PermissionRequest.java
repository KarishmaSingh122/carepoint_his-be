package com.suma.carepoint.models.auth;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PermissionRequest {

    @NotBlank
    @Size(max = 100)
    private String permissionName;

    @Size(max = 100)
    private String moduleName;

    @Size(max = 30)
    private String action;

    @Size(max = 5000)
    private String description;
}
