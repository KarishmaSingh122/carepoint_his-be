package com.suma.carepoint.services.billing;

import com.suma.carepoint.models.ApiResponse;
import com.suma.carepoint.models.billing.BillResponse;
import com.suma.carepoint.models.billing.CreateBillRequest;
import org.jspecify.annotations.Nullable;

public interface BillService {

    BillResponse createBill(CreateBillRequest request);

    BillResponse getBillByBillId(Long billId);

    BillResponse getBillByBillNo(String billNo);
}
