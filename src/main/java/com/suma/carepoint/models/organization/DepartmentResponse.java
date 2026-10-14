package com.suma.carepoint.models.organization;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentResponse {

    private Long departmentId;
    private String departmentCode;
    private String departmentName;
    private String description;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
}
