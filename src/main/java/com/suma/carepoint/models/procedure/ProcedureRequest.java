package com.suma.carepoint.models.procedure;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcedureRequest {

    @NotBlank
    private String procedureCode;

    @NotBlank
    private String procedureName;

    private String description;
}
