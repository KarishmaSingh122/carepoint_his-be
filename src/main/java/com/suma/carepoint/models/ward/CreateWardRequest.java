package com.suma.carepoint.models.ward;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateWardRequest {

    private Long departmentId;

    private Long floorId;

    private String wardName;

    private WardType wardType;

    private Boolean active;
}
