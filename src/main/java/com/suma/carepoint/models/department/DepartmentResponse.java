package com.suma.carepoint.models.department;

import lombok.*;

import java.time.Instant;
import java.time.OffsetDateTime;

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
