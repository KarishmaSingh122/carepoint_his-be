package com.suma.carepoint.entities.procedure;


import com.suma.carepoint.entities.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "procedures")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Procedure extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "procedure_id")
    private Long procedureId;

    @Column(name = "procedure_code", nullable = false, unique = true, length = 30)
    private String procedureCode;

    @Column(name = "procedure_name", nullable = false, unique = true, length = 200)
    private String procedureName;

    @Column(name = "description")
    private String description;

    @Column(name = "active", nullable = false)
    private Boolean active = true;
}