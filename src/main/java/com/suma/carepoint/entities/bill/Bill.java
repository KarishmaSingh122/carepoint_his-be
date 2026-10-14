package com.suma.carepoint.entities.bill;

import com.suma.carepoint.entities.base.BaseEntity;
import com.suma.carepoint.entities.patient.Patient;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "bills")
public class Bill extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bill_id")
    private Long billId;

    @Column(name = "bill_no", nullable = false, unique = true, length = 30)
    private String billNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "visit_id")
//    private Visit visit;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "admission_id")
//    private Admission admission;

    @Column(name = "bill_date", nullable = false)
    private OffsetDateTime billDate;

    @Column(name = "subtotal", nullable = false, precision = 12, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "discount", nullable = false, precision = 12, scale = 2)
    private BigDecimal discount;

    @Column(name = "tax", nullable = false, precision = 12, scale = 2)
    private BigDecimal tax;

    @Column(name = "total_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private EBillStatus status;

    @OneToMany(
            mappedBy = "bill",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<BillItem> items = new ArrayList<>();

    @OneToMany(
            mappedBy = "bill",
            cascade = CascadeType.ALL
    )
    @Builder.Default
    private List<Payment> payments = new ArrayList<>();
}
