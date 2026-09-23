package com.suma.carepoint.models.floor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

    public class CreateFloorRequest {

        private Long floorNumber;
        private String floorName;
        private String description;
        private Boolean active;
    }

