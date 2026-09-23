package com.suma.carepoint.models.organization;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StaffResponse {

    private Long staffId;
    private String employeeNo;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String designation;
    private String specialization;
    private LocalDate joiningDate;
    private boolean active;
    private DepartmentResponse department;
    private Instant createdAt;
    private Instant updatedAt;

}
