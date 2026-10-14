package com.suma.carepoint.entities.wardrooms;
import com.suma.carepoint.entities.ward.Ward;
import com.suma.carepoint.models.wardrooms.RoomType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "rooms")
public class WardRoom {

    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    @Column(name = "room_id")
    private Long  roomId ;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ward_id")
    private Ward ward ;

    @Column(name="room_number" , unique = true, nullable = false)
    private String roomNumber;

    @Enumerated(EnumType.STRING)
    @Column(name="room_type")
    private RoomType roomType;

    @Column(name="status")
    private String status ;

}
