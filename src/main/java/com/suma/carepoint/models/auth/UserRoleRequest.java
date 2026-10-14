package com.suma.carepoint.models.auth;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRoleRequest {

    @NotNull
    @Positive
    private Long userId;

    @NotNull
    @Positive
    private Long roleId;
}
