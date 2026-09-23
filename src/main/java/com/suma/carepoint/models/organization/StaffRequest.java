package com.suma.carepoint.models.organization;


import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

import static com.suma.carepoint.models.constants.ApiConstant.Regexp.ALPHANUMERIC_DASH_UNDERSCORE;
import static com.suma.carepoint.models.constants.ApiConstant.Regexp.CONTACT_NUMBER;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StaffRequest {

    @NotBlank(message = "Employee number is required")
    @Size(max = 30, message = "Employee number must not exceed 30 characters")
    @Pattern(regexp = ALPHANUMERIC_DASH_UNDERSCORE, message = "Employee number contains invalid characters")
    private String employeeNo;

    @NotBlank(message = "First name is required")
    @Size(max = 100, message = "First name must not exceed 100 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 100, message = "Last name must not exceed 100 characters")
    private String lastName;

    @Size(max = 20, message = "Phone must not exceed 20 characters")
    @Pattern(regexp = CONTACT_NUMBER, message = "Phone contains invalid characters")
    private String phone;

    @Email(message = "Invalid email address")
    @Size(max = 150, message = "Email must not exceed 150 characters")
    private String email;

    @Size(max = 100, message = "Designation must not exceed 100 characters")
    private String designation;

    @Size(max = 100, message = "Specialization must not exceed 100 characters")
    private String specialization;

    @NotNull(message = "Department ID is required")
    private Long departmentId;

    @PastOrPresent(message = "Joining date cannot be in the future")
    private LocalDate joiningDate;
}
