package com.suma.carepoint.models.wardrooms;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class RoomResponse {

    private Long roomId;
    private Long wardId ;
    private String roomNumber;
    private RoomType roomType;
    private String status;

}
