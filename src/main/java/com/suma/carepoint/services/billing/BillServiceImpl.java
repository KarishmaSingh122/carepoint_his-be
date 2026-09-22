package com.suma.carepoint.services.billing;

import com.suma.carepoint.entities.bill.Bill;
import com.suma.carepoint.entities.bill.BillItem;
import com.suma.carepoint.entities.bill.EBillStatus;
import com.suma.carepoint.entities.bill.Payment;
import com.suma.carepoint.entities.medical_service.HospitalService;
import com.suma.carepoint.entities.patient.Patient;
import com.suma.carepoint.models.billing.*;
import com.suma.carepoint.repositories.billing.BillItemRepository;
import com.suma.carepoint.repositories.billing.BillRepository;
import com.suma.carepoint.repositories.billing.PaymentRepository;
import com.suma.carepoint.repositories.medical_service.ServiceRepository;
import com.suma.carepoint.repositories.patient.PatientRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class BillServiceImpl implements BillService{
    private final ModelMapper modelMapper;
    private final BillRepository billRepository;
    private final BillItemRepository billItemRepository;
    private final PaymentRepository paymentRepository;
    private final PatientRepository patientRepository;
    private final ServiceRepository serviceRepository;


    public BillServiceImpl(BillRepository billRepository,
                           BillItemRepository billItemRepository,
                           PaymentRepository paymentRepository,
                           PatientRepository patientRepository,
                           ModelMapper modelMapper,
                           ServiceRepository serviceRepository){
        this.modelMapper = modelMapper;
        this.billItemRepository= billItemRepository;
        this.billRepository = billRepository;
        this.paymentRepository = paymentRepository;
        this.patientRepository = patientRepository;
        this.serviceRepository = serviceRepository;
    }



    @Override
    @Transactional
    public BillResponse createBill(CreateBillRequest request) {

        log.info("Creating bill for patientId: {}", request.getPatientId());

        // ---------------------------------------------------------
        // 1. Validate request
        // ---------------------------------------------------------

        if (request.getPatientId() == null) {
            throw new IllegalArgumentException("Patient ID is required");
        }

        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new IllegalArgumentException(
                    "At least one billing item is required"
            );
        }

        // ---------------------------------------------------------
        // 2. Find Patient
        // ---------------------------------------------------------

        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Patient not found with id: "
                                        + request.getPatientId()
                        )
                );

        // ---------------------------------------------------------
        // 3. Create Bill
        // ---------------------------------------------------------

        Bill bill = new Bill();

        bill.setBillNo(generateBillNumber());

        bill.setPatient(patient);

        /*
         * Set these only if the corresponding entities exist
         * and you want to validate them.
         */
//        bill.setVisitId(request.getVisitId());
//        bill.setAdmissionId(request.getAdmissionId());

        bill.setBillDate(
                OffsetDateTime.now(ZoneOffset.UTC)
        );

        // ---------------------------------------------------------
        // 4. Discount and Tax
        // ---------------------------------------------------------

        BigDecimal discount = request.getDiscount() != null
                ? request.getDiscount()
                : BigDecimal.ZERO;

        BigDecimal tax = request.getTax() != null
                ? request.getTax()
                : BigDecimal.ZERO;

        if (discount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Discount cannot be negative"
            );
        }

        if (tax.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Tax cannot be negative"
            );
        }

        // ---------------------------------------------------------
        // 5. Create Bill Items
        // ---------------------------------------------------------

        List<BillItem> billItems = new ArrayList<>();

        BigDecimal subtotal = BigDecimal.ZERO;

        for (CreateBillItemRequest itemRequest : request.getItems()) {

            if (itemRequest.getServiceId() == null) {
                throw new IllegalArgumentException(
                        "Service ID is required"
                );
            }

            if (itemRequest.getQuantity() == null
                    || itemRequest.getQuantity() <= 0) {

                throw new IllegalArgumentException(
                        "Quantity must be greater than zero"
                );
            }

            // -----------------------------------------------------
            // Fetch service from DB
            // -----------------------------------------------------

            HospitalService service = serviceRepository
                    .findById(itemRequest.getServiceId())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Service not found with id: "
                                            + itemRequest.getServiceId()
                            )
                    );

            // -----------------------------------------------------
            // Get price from DB
            // -----------------------------------------------------

            BigDecimal unitPrice = service.getDefaultPrice();

            if (unitPrice == null) {
                throw new RuntimeException(
                        "Price not configured for service: "
                                + service.getServiceName()
                );
            }

            // -----------------------------------------------------
            // Calculate amount
            // -----------------------------------------------------

            BigDecimal amount = unitPrice.multiply(
                    BigDecimal.valueOf(itemRequest.getQuantity())
            );

            BillItem billItem = new BillItem();

            billItem.setBill(billItem == null ? null : bill);
            billItem.setService(service);
            billItem.setQuantity(itemRequest.getQuantity());
            billItem.setUnitPrice(unitPrice);
            billItem.setUnitPrice(BigDecimal.ZERO);

            billItem.setDiscount(BigDecimal.ZERO);
            billItem.setAmount(amount);

            billItems.add(billItem);

            subtotal = subtotal.add(amount);
        }

        // ---------------------------------------------------------
        // 6. Calculate Total
        // ---------------------------------------------------------

        BigDecimal totalAmount = subtotal
                .subtract(discount)
                .add(tax);

        if (totalAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Total amount cannot be negative"
            );
        }

        // ---------------------------------------------------------
        // 7. Set Bill Amounts
        // ---------------------------------------------------------

        bill.setSubtotal(subtotal);
        bill.setDiscount(discount);
        bill.setTax(tax);
        bill.setTotalAmount(totalAmount);

        bill.setStatus(EBillStatus.PENDING);

        // ---------------------------------------------------------
        // 8. Save Bill
        // ---------------------------------------------------------

        Bill savedBill = billRepository.save(bill);

        // ---------------------------------------------------------
        // 9. Set Bill reference and save Items
        // ---------------------------------------------------------

        for (BillItem billItem : billItems) {
            billItem.setBill(savedBill);
        }

        billItemRepository.saveAll(billItems);

        log.info(
                "Bill created successfully. billId={}, billNo={}",
                savedBill.getBillId(),
                savedBill.getBillNo()
        );

        // ---------------------------------------------------------
        // 10. Return complete BillResponse
        // ---------------------------------------------------------

        return getBillByBillId(savedBill.getBillId());
    }

    // =============================================================
    // Generate Bill Number
    // =============================================================

    private String generateBillNumber() {

        return "BILL-"
                + System.currentTimeMillis();
    }


    @Override
    public BillResponse getBillByBillId(Long billId) {

        log.info("Fetching bill by billId: {}", billId);

        Bill bill = billRepository.findByBillId(billId).orElseThrow(() ->
                        new RuntimeException("Bill not found with id: " + billId)
                );

        return buildBillResponse(bill);
    }

    @Override
    public BillResponse getBillByBillNo(String billNo) {

        log.info("Fetching bill by billNo: {}", billNo);

        Bill bill = billRepository.findByBillNo(billNo)
                .orElseThrow(() ->
                        new RuntimeException("Bill not found with bill number: " + billNo)
                );

        return buildBillResponse(bill);
    }

//    private BillResponse buildBillResponse(Bill bill) {
//
//        /*
//         * ---------------------------------------------------------
//         * 1. Map basic Bill information
//         * ---------------------------------------------------------
//         */
//        BillResponse response = modelMapper.map(bill, BillResponse.class);
//
//        /*
//         * ---------------------------------------------------------
//         * 2. Fetch Bill Items
//         * ---------------------------------------------------------
//         */
//        List<BillItem> billItems =
//                billItemRepository.findByBillBillId(bill.getBillId());
//
//        List<BillItemResponse> itemResponses = billItems.stream()
//                .map(item -> {
//
//                    BillItemResponse itemResponse =
//                            modelMapper.map(item, BillItemResponse.class);
//
//                    /*
//                     * Service information
//                     */
//                    if (item.getService() != null) {
//                        itemResponse.setServiceId(
//                                item.getService().getServiceId()
//                        );
//
//                        itemResponse.setServiceName(
//                                item.getService().getServiceName()
//                        );
//                    }
//
//                    return itemResponse;
//                })
//                .toList();
//
//        response.setItems(itemResponses);
//
//        /*
//         * ---------------------------------------------------------
//         * 3. Fetch Payments
//         * ---------------------------------------------------------
//         */
//        List<Payment> payments =
//                paymentRepository.findByBillBillId(bill.getBillId());
//
//        List<PaymentResponse> paymentResponses = payments.stream()
//                .map(payment ->
//                        modelMapper.map(payment, PaymentResponse.class)
//                )
//                .toList();
//
//        response.setPayments(paymentResponses);
//
//        /*
//         * ---------------------------------------------------------
//         * 4. Calculate Paid Amount
//         * ---------------------------------------------------------
//         *
//         * Only successful payments should contribute to the
//         * amount paid.
//         */
//        BigDecimal paidAmount = payments.stream()
//                .filter(payment ->
//                        payment.getPaymentStatus() != null
//                                && payment.getPaymentStatus().name().equals("SUCCESS")
//                )
//                .map(Payment::getAmount)
//                .filter(amount -> amount != null)
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
//
//        /*
//         * ---------------------------------------------------------
//         * 5. Calculate Balance
//         * ---------------------------------------------------------
//         */
//        BigDecimal totalAmount =
//                bill.getTotalAmount() != null
//                        ? bill.getTotalAmount()
//                        : BigDecimal.ZERO;
//
//        BigDecimal balanceAmount =
//                totalAmount.subtract(paidAmount);
//
//        /*
//         * Avoid negative balance
//         */
//        if (balanceAmount.compareTo(BigDecimal.ZERO) < 0) {
//            balanceAmount = BigDecimal.ZERO;
//        }
//
//        response.setPaidAmount(paidAmount);
//        response.setBalanceAmount(balanceAmount);
//
//        return response;
//    }

    private BillResponse buildBillResponse(Bill bill) {

        /*
         * ---------------------------------------------------------
         * 1. Build basic Bill response manually
         * ---------------------------------------------------------
         */
        BillResponse response = BillResponse.builder()
                .billId(bill.getBillId())
                .billNo(bill.getBillNo())
                .billDate(bill.getBillDate())
                .subtotal(bill.getSubtotal())
                .discount(bill.getDiscount())
                .tax(bill.getTax())
                .totalAmount(bill.getTotalAmount())
                .status(bill.getStatus())
                .build();

        /*
         * ---------------------------------------------------------
         * 2. Set Patient ID
         * ---------------------------------------------------------
         */
        if (bill.getPatient() != null) {
            response.setPatientId(
                    bill.getPatient().getPatientId()
            );
        }

        /*
         * ---------------------------------------------------------
         * 3. Set Visit ID
         * ---------------------------------------------------------
         */
//        if (bill.getVisit() != null) {
//            response.setVisitId(
//                    bill.getVisit().getVisitId()
//            );
//        }

        /*
         * ---------------------------------------------------------
         * 4. Set Admission ID
         * ---------------------------------------------------------
         */
//        if (bill.getAdmission() != null) {
//            response.setAdmissionId(
//                    bill.getAdmission().getAdmissionId()
//            );
//        }

        /*
         * ---------------------------------------------------------
         * 5. Fetch Bill Items
         * ---------------------------------------------------------
         */
        List<BillItem> billItems =
                billItemRepository.findByBillBillId(
                        bill.getBillId()
                );

        List<BillItemResponse> itemResponses =
                billItems.stream()
                        .map(item -> {

                            BillItemResponse itemResponse =
                                    modelMapper.map(
                                            item,
                                            BillItemResponse.class
                                    );

                            /*
                             * Service information
                             */
                            if (item.getService() != null) {

                                itemResponse.setServiceId(
                                        item.getService().getServiceId()
                                );

                                itemResponse.setServiceName(
                                        item.getService().getServiceName()
                                );
                            }

                            return itemResponse;
                        })
                        .toList();

        response.setItems(itemResponses);

        /*
         * ---------------------------------------------------------
         * 6. Fetch Payments
         * ---------------------------------------------------------
         */
        List<Payment> payments =
                paymentRepository.findByBillBillId(
                        bill.getBillId()
                );

        List<PaymentResponse> paymentResponses =
                payments.stream()
                        .map(payment ->
                                modelMapper.map(
                                        payment,
                                        PaymentResponse.class
                                )
                        )
                        .toList();

        response.setPayments(paymentResponses);

        /*
         * ---------------------------------------------------------
         * 7. Calculate Paid Amount
         * ---------------------------------------------------------
         */
        BigDecimal paidAmount =
                payments.stream()
                        .filter(payment ->
                                payment.getPaymentStatus() != null
                                        && payment.getPaymentStatus()
                                        .name()
                                        .equals("SUCCESS")
                        )
                        .map(Payment::getAmount)
                        .filter(Objects::nonNull)
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

        /*
         * ---------------------------------------------------------
         * 8. Calculate Balance
         * ---------------------------------------------------------
         */
        BigDecimal totalAmount =
                bill.getTotalAmount() != null
                        ? bill.getTotalAmount()
                        : BigDecimal.ZERO;

        BigDecimal balanceAmount =
                totalAmount.subtract(paidAmount);

        /*
         * Avoid negative balance
         */
        if (balanceAmount.compareTo(BigDecimal.ZERO) < 0) {
            balanceAmount = BigDecimal.ZERO;
        }

        response.setPaidAmount(paidAmount);
        response.setBalanceAmount(balanceAmount);

        return response;
    }
}