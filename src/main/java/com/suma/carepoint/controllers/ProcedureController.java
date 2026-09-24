package com.suma.carepoint.controllers;

import com.suma.carepoint.models.ApiResponse;
import com.suma.carepoint.models.constants.ApiConstant;
import com.suma.carepoint.models.procedure.PatientTreatmentRequest;
import com.suma.carepoint.models.procedure.ProcedureRequest;
import com.suma.carepoint.services.procedure.ProcedureService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiConstant.Controller.PROCEDURE)
@AllArgsConstructor
public class ProcedureController {

    @Autowired
    private ProcedureService procedureService;

    //apis related to procedures

    @PostMapping(ApiConstant.Procedure.PROCEDURE)
    public ResponseEntity<ApiResponse> createProcedure(@Valid @RequestBody ProcedureRequest request) {
        return ResponseEntity.ok().body(procedureService.createProcedure(request));
    }

    @GetMapping(ApiConstant.Procedure.PROCEDURE)
    public ResponseEntity<ApiResponse> getProcedures() {
        return ResponseEntity.ok().body(procedureService.getProcedures());
    }


    /**
     * Get Procedure By ID
     */
    @GetMapping(ApiConstant.Procedure.PROCEDURE + "/{procedureId}")
    public ResponseEntity<ApiResponse> getProcedureById(
            @PathVariable("procedureId") Long procedureId) {

        return ResponseEntity.ok()
                .body(procedureService.getProcedureById(procedureId));
    }


    /**
     * Update Procedure
     * PUT /api/procedures/{procedureId}
     */
    @PutMapping(ApiConstant.Procedure.PROCEDURE + "/{procedureId}")
    public ResponseEntity<ApiResponse> updateProcedure(
            @PathVariable("procedureId") Long procedureId,
            @Valid @RequestBody ProcedureRequest request) {
        return ResponseEntity.ok().body(procedureService.updateProcedure(procedureId, request));
    }


    /**
     * Activate / Deactivate Procedure
     */
    @PatchMapping(
            ApiConstant.Procedure.PROCEDURE
                    + "/{procedureId}/status"
    )
    public ResponseEntity<ApiResponse> updateProcedureStatus(
            @PathVariable("procedureId") Long procedureId,
            @RequestParam("active") Boolean active) {

        return ResponseEntity.ok()
                .body(procedureService.updateProcedureStatus(
                        procedureId,
                        active
                ));
    }


    /**
     * Delete Procedure
     */
    @DeleteMapping(
            ApiConstant.Procedure.PROCEDURE
                    + "/{procedureId}"
    )
    public ResponseEntity<ApiResponse> deleteProcedure(
            @PathVariable("procedureId") Long procedureId) {

        return ResponseEntity.ok()
                .body(procedureService.deleteProcedure(procedureId));
    }


    /**
     * Search Procedures
     */
    @GetMapping(ApiConstant.Procedure.PROCEDURE + "/search")
    public ResponseEntity<ApiResponse> searchProcedures(
            @RequestParam("keyword") String keyword) {

        return ResponseEntity.ok()
                .body(procedureService.searchProcedures(keyword));
    }



    /**
     * Create Treatment
     */
    @PostMapping(ApiConstant.Procedure.TREATMENT)
    public ResponseEntity<ApiResponse> createTreatment(
            @Valid @RequestBody PatientTreatmentRequest request) {

        return ResponseEntity.ok()
                .body(procedureService.createTreatment(request));
    }


    /**
     * Get Treatment By ID
     */
    @GetMapping(ApiConstant.Procedure.TREATMENT + "/{treatmentId}")
    public ResponseEntity<ApiResponse> getTreatmentById(
            @PathVariable("treatmentId") Long treatmentId) {

        return ResponseEntity.ok()
                .body(procedureService.getTreatmentById(treatmentId));
    }


    /**
     * Update Treatment
     */
    @PutMapping(ApiConstant.Procedure.TREATMENT + "/{treatmentId}")
    public ResponseEntity<ApiResponse> updateTreatment(
            @PathVariable("treatmentId") Long treatmentId,
            @Valid @RequestBody PatientTreatmentRequest request) {

        return ResponseEntity.ok()
                .body(procedureService.updateTreatment(
                        treatmentId,
                        request
                ));
    }


    /**
     * Delete Treatment
     */
    @DeleteMapping(ApiConstant.Procedure.TREATMENT + "/{treatmentId}")
    public ResponseEntity<ApiResponse> deleteTreatment(
            @PathVariable("treatmentId") Long treatmentId) {

        return ResponseEntity.ok()
                .body(procedureService.deleteTreatment(treatmentId));
    }


    /**
     * Get Treatments By AdmissionId
     */
    @GetMapping(
            ApiConstant.Procedure.ADMISSION_TREATMENTS+"/{admissionId}"
    )
    public ResponseEntity<ApiResponse> getTreatmentsByAdmission(
            @PathVariable("admissionId") Long admissionId) {

        return ResponseEntity.ok()
                .body(procedureService.getTreatmentsByAdmission(admissionId));
    }


    /**
     * Get Treatments By DoctorId
     */
    @GetMapping(
            ApiConstant.Procedure.DOCTOR_TREATMENTS+"/{doctorId}"
    )
    public ResponseEntity<ApiResponse> getTreatmentsByDoctor(
            @PathVariable("doctorId") Long doctorId) {

        return ResponseEntity.ok()
                .body(procedureService.getTreatmentsByDoctor(doctorId));
    }


    /**
     * Get Treatments By ProcedureId
     */
    @GetMapping(
            ApiConstant.Procedure.PROCEDURE_TREATMENTS+"/{procedureId}"
    )
    public ResponseEntity<ApiResponse> getTreatmentsByProcedure(
            @PathVariable("procedureId") Long procedureId) {

        return ResponseEntity.ok()
                .body(procedureService.getTreatmentsByProcedure(procedureId));
    }


}
