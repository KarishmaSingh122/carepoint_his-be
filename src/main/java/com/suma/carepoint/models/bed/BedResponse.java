package com.suma.carepoint.models.bed;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BedResponse {

    private Long bedId;
    private Long roomId;
    private String bedNumber;
    private String status;
}
