package com.suma.carepoint.services.billing;

import com.suma.carepoint.models.billing.BillResponse;
import com.suma.carepoint.models.billing.CreateBillRequest;

import java.util.List;

public interface BillService {

    BillResponse createBill(CreateBillRequest request);

    BillResponse getBillByBillId(Long billId);

    BillResponse getBillByBillNo(String billNo);

    List<BillResponse> getBills();
}
