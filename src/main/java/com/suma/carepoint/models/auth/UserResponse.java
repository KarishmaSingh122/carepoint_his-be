package com.suma.carepoint.models.auth;


import lombok.*;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {

    private Long userId;
    private Long staffId;
    private String employeeNo;
    private String firstName;
    private String lastName;
    private String username;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
}
