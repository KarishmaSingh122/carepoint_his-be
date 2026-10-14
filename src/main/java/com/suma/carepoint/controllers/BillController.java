package com.suma.carepoint.controllers;

import com.suma.carepoint.models.ApiResponse;
import com.suma.carepoint.models.billing.BillResponse;
import com.suma.carepoint.models.billing.CreateBillRequest;
import com.suma.carepoint.models.constants.ApiConstant;
import com.suma.carepoint.services.billing.BillService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiConstant.Controller.BILL)
@AllArgsConstructor
public class BillController {

    @Autowired
    private final BillService billService;

    @PostMapping(ApiConstant.Bill.BILLS)
    public ResponseEntity<ApiResponse> createBill(@RequestBody CreateBillRequest request) {
        BillResponse billResponse = billService.createBill( request);
        return ResponseEntity.ok().body(new ApiResponse(1, "", billResponse));

    }

    @GetMapping(ApiConstant.Bill.BILLS)
    public ResponseEntity<ApiResponse> getBills() {
        List<BillResponse> billResponseList = billService.getBills();
        return ResponseEntity.ok().body(new ApiResponse(1, "", billResponseList));

    }

    @GetMapping(ApiConstant.Bill.BILLS+"/{billId}")
    public ResponseEntity<ApiResponse> getBillByBillId(@PathVariable(name = "billId") Long billId) {
        BillResponse billResponse = billService.getBillByBillId(billId);
        return ResponseEntity.ok().body(new ApiResponse(1, "", billResponse));

    }

    @GetMapping(ApiConstant.Bill.BILLS_BY_NUMBER+"/{billNo}")
    public ResponseEntity<ApiResponse> getBillByBillNo(@PathVariable(name = "billNo") String billNo) {
        BillResponse billResponse = billService.getBillByBillNo(billNo);
        return ResponseEntity.ok().body(new ApiResponse(1, "", billResponse));
    }
}
