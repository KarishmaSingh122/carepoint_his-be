package com.suma.carepoint.repositories.billing;

import com.suma.carepoint.entities.bill.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long> {
    List<Payment> findByBillBillId(Long billId);
}
