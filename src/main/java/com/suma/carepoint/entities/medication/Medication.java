package com.suma.carepoint.entities.medication;

import com.suma.carepoint.entities.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "medications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Medication  extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "medication_id")
    private Long medicationId;

    @Column(name = "name", nullable = false, unique = true, length = 150)
    private String name;

    @Column(name = "generic_name", length = 150)
    private String genericName;

    @Column(name = "strength", length = 50)
    private String strength;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

//    @Column(name = "created_at", nullable = false, updatable = false)
//    private OffsetDateTime createdAt;
//
//    @Column(name = "updated_at", nullable = false)
//    private OffsetDateTime updatedAt;
}
