package com.suma.carepoint.models.organization;

import com.suma.carepoint.models.constants.ApiConstant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentRequest {

    @NotBlank(message = "Department code is required")
    @Size(max = 20, message = "Department code must not exceed 20 characters")
    @Pattern(regexp = ApiConstant.Regexp.ALPHANUMERIC_DASH_UNDERSCORE,
            message = "Department code contains invalid characters")
    private String departmentCode;

    @NotBlank(message = "Department name is required")
    @Size(max = 100, message = "Department name must not exceed 100 characters")
    private String departmentName;

    @Size(max = 2000, message = "Description must not exceed 2000 characters")
    private String description;
}