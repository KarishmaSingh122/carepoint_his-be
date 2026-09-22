package com.suma.carepoint.entities.bill;

import com.suma.carepoint.entities.base.BaseEntity;
import com.suma.carepoint.entities.medical_service.HospitalService;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "bill_items")
public class BillItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bill_item_id")
    private Long billItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bill_id", nullable = false)
    private Bill bill;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private HospitalService service;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "unit_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "discount", nullable = false, precision = 12, scale = 2)
    private BigDecimal discount;

    @Column(name = "amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

//    @Column(name = "created_at", nullable = false)
//    private OffsetDateTime createdAt;
}
