package com.suma.carepoint.services.medication;

import com.suma.carepoint.models.ApiResponse;
import com.suma.carepoint.models.medication.CreateMedicationRequest;
import com.suma.carepoint.models.medication.MedicationStatusRequest;
import com.suma.carepoint.models.medication.PrescriptionRequest;
import com.suma.carepoint.models.medication.UpdateMedicationRequest;

public interface MedicationService {

    //medication related methods

    ApiResponse createMedication(
            CreateMedicationRequest request
    );

    ApiResponse getMedications();

    ApiResponse getMedicationById(
            Long medicationId
    );

    ApiResponse updateMedication(
            Long medicationId,
            UpdateMedicationRequest request
    );

    ApiResponse updateMedicationStatus(
            Long medicationId,
            MedicationStatusRequest request
    );

    ApiResponse deleteMedication(
            Long medicationId
    );

    ApiResponse searchMedications(
            String keyword
    );

    //prescription related methods

    ApiResponse createPrescription(PrescriptionRequest request);

    ApiResponse getPrescriptionById(Long prescriptionId);

    ApiResponse updatePrescription(
            Long prescriptionId,
            PrescriptionRequest request
    );

    ApiResponse updatePrescriptionStatus(
            Long prescriptionId,
            Boolean active
    );

    ApiResponse deletePrescription(Long prescriptionId);

    ApiResponse getPatientPrescriptions(Long patientId);

    ApiResponse getVisitPrescriptions(Long visitId);

    ApiResponse getDoctorPrescriptions(Long doctorId);
}
