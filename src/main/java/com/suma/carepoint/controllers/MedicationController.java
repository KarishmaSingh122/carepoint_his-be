package com.suma.carepoint.controllers;

import com.suma.carepoint.models.ApiResponse;
import com.suma.carepoint.models.constants.ApiConstant;
import com.suma.carepoint.models.medication.CreateMedicationRequest;
import com.suma.carepoint.models.medication.MedicationStatusRequest;
import com.suma.carepoint.models.medication.PrescriptionRequest;
import com.suma.carepoint.models.medication.UpdateMedicationRequest;
import com.suma.carepoint.services.medication.MedicationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiConstant.Controller.MEDICATION)
@AllArgsConstructor
public class MedicationController {
    @Autowired
    private MedicationService medicationService;

    @PostMapping(ApiConstant.Medication.MEDICATIONS)
    public ResponseEntity<ApiResponse> createMedication(@Valid @RequestBody CreateMedicationRequest request) {
        return ResponseEntity.ok().body(medicationService.createMedication(request));
    }

    @GetMapping(ApiConstant.Medication.MEDICATIONS)
    public ResponseEntity<ApiResponse> getMedications() {
        return ResponseEntity.ok().body(medicationService.getMedications());
    }

    @GetMapping(ApiConstant.Medication.MEDICATIONS+"/{medicationId}")
    public ResponseEntity<ApiResponse> getMedicationById(@PathVariable("medicationId") Long medicationId) {
        return ResponseEntity.ok().body(medicationService.getMedicationById(medicationId));
    }

    @PutMapping(ApiConstant.Medication.MEDICATIONS+"/{id}")
    public ResponseEntity<ApiResponse> updateMedication(
            @PathVariable("id") Long medicationId,
            @Valid @RequestBody UpdateMedicationRequest request
    ) {
        return ResponseEntity.ok( medicationService.updateMedication(medicationId,request));
    }

    @PatchMapping(ApiConstant.Medication.MEDICATIONS+"/{medicationId}/status")
    public ResponseEntity<ApiResponse> updateMedicationStatus(
            @PathVariable("medicationId") Long medicationId,
            @Valid @RequestBody MedicationStatusRequest request
    ) {
        return ResponseEntity.ok(medicationService.updateMedicationStatus(medicationId, request)
        );
    }

    @DeleteMapping(ApiConstant.Medication.MEDICATIONS+"/{medicationId}")
    public ResponseEntity<ApiResponse> deleteMedication(
            @PathVariable("medicationId") Long medicationId
    ) {
        return ResponseEntity.ok(medicationService.deleteMedication(medicationId));
    }

    @GetMapping(ApiConstant.Medication.MEDICATIONS+"/search")
    public ResponseEntity<ApiResponse> searchMedications(
            @RequestParam("keyword") String keyword
    ) {
        return ResponseEntity.ok(medicationService.searchMedications(keyword));
    }


    @PostMapping(ApiConstant.Medication.PRESCRIPTION)
    public ResponseEntity<ApiResponse> createPrescription(@Valid @RequestBody PrescriptionRequest request) {
        return ResponseEntity.ok().body(medicationService.createPrescription(request));
    }

//    @GetMapping(ApiConstant.Medication.MEDICATIONS)
//    public ResponseEntity<ApiResponse> getPriscriptions() {
//        return ResponseEntity.ok().body(medicationService.getPriscriptions());
//    }

    @GetMapping(ApiConstant.Medication.PRESCRIPTION+"/{prescriptionId}")
    public ResponseEntity<ApiResponse> getPrescriptionById(@PathVariable("prescriptionId") Long prescriptionId) {
        return ResponseEntity.ok().body(medicationService.getPrescriptionById(prescriptionId));
    }


    /**
     * Update Prescription
     * PUT /api/prescriptions/{prescriptionId}
     */
    @PutMapping(ApiConstant.Medication.PRESCRIPTION + "/{prescriptionId}")
    public ResponseEntity<ApiResponse> updatePrescription(
            @PathVariable("prescriptionId") Long prescriptionId,
            @Valid @RequestBody PrescriptionRequest request) {

        return ResponseEntity.ok()
                .body(medicationService.updatePrescription(
                        prescriptionId,
                        request
                ));
    }


    /**
     * Activate / Deactivate Prescription
     * PATCH /api/prescriptions/{prescriptionId}/status
     */
    @PatchMapping(
            ApiConstant.Medication.PRESCRIPTION
                    + "/{prescriptionId}/status"
    )
    public ResponseEntity<ApiResponse> updatePrescriptionStatus(
            @PathVariable("prescriptionId") Long prescriptionId,
            @RequestParam("active") Boolean active) {

        return ResponseEntity.ok()
                .body(medicationService.updatePrescriptionStatus(
                        prescriptionId,
                        active
                ));
    }


    /**
     * Delete Prescription
     * DELETE /api/prescriptions/{prescriptionId}
     */
    @DeleteMapping(ApiConstant.Medication.PRESCRIPTION + "/{prescriptionId}")
    public ResponseEntity<ApiResponse> deletePrescription(@PathVariable("prescriptionId") Long prescriptionId) {

        return ResponseEntity.ok().body(medicationService.deletePrescription(prescriptionId));
    }


    /**
     * Get Patient Prescription History
     * GET /api/patients/{patientId}/prescriptions
     */
    @GetMapping(ApiConstant.Medication.PATIENT_PRESCRIPTIONS + "/{patientId}")
    public ResponseEntity<ApiResponse> getPatientPrescriptions(@PathVariable("patientId") Long patientId) {

        return ResponseEntity.ok().body(medicationService.getPatientPrescriptions(patientId));
    }


    /**
     * Get Visit Prescriptions
     * GET /api/visits/{visitId}/prescriptions
     */
    @GetMapping(ApiConstant.Medication.VISIT_PRESCRIPTIONS + "/{visitId}")
    public ResponseEntity<ApiResponse> getVisitPrescriptions(@PathVariable("visitId") Long visitId) {
        return ResponseEntity.ok().body(medicationService.getVisitPrescriptions(visitId));
    }


    /**
     * Get Doctor Prescriptions
     * GET /api/doctors/{doctorId}/prescriptions
     */
    @GetMapping(ApiConstant.Medication.DOCTOR_PRESCRIPTIONS + "/{doctorId}")
    public ResponseEntity<ApiResponse> getDoctorPrescriptions(@PathVariable("doctorId") Long doctorId) {
        return ResponseEntity.ok().body(medicationService.getDoctorPrescriptions(doctorId));
    }
}