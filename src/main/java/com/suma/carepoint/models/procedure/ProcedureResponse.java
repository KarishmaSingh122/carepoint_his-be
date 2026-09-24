package com.suma.carepoint.models.procedure;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcedureResponse {

    private Long procedureId;

    private String procedureCode;

    private String procedureName;

    private String description;

    private Boolean active;

    private Instant createdAt;

    private Instant updatedAt;
}
