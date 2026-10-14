package com.suma.carepoint.entities.ward;


import com.suma.carepoint.entities.base.BaseEntity;
import com.suma.carepoint.entities.floor.Floor;
import com.suma.carepoint.entities.organization.Department;
import com.suma.carepoint.models.ward.WardType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name="wards")
public class Ward  extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ward_id")
    private Long wardId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "floor_id", nullable = false)
    private Floor floor;

    @Column(name = "ward_name", nullable = false, length = 100)
    private String wardName;

    @Enumerated(EnumType.STRING)
    @Column(name = "ward_type", nullable = false, length = 50)
    private WardType wardType;

    @Column(name = "active", nullable = false)
    private Boolean active = true;
}

