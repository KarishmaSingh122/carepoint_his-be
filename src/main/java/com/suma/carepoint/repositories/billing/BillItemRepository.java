package com.suma.carepoint.repositories.billing;

import com.suma.carepoint.entities.bill.BillItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillItemRepository extends JpaRepository<BillItem,Long> {
    List<BillItem> findByBillBillId(Long billId);
}
