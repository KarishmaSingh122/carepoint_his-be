package com.suma.carepoint.entities.bed;

import com.suma.carepoint.entities.wardrooms.WardRoom;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "beds", uniqueConstraints = {@UniqueConstraint(name = "uq_beds_room_bedno",
        columnNames = {"room_id", "bed_number"})})
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Bed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bed_id")
    private Long bedId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private WardRoom room;

    @Column(name = "bed_number", length = 20, nullable = false)
    private String bedNumber;

    @Column(name = "status", length = 20, nullable = false)
    private String status;
}

