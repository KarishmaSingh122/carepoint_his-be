package com.suma.carepoint.models.auth;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleRequest {

    @NotBlank
    @Size(max = 50)
    private String roleName;

    @Size(max = 1000)
    private String description;
}
