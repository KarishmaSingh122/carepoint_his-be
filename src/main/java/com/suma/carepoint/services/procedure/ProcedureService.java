package com.suma.carepoint.services.procedure;

import com.suma.carepoint.models.ApiResponse;
import com.suma.carepoint.models.procedure.PatientTreatmentRequest;
import com.suma.carepoint.models.procedure.ProcedureRequest;


public interface ProcedureService {
    //procedure code

     ApiResponse createProcedure( ProcedureRequest request);

     ApiResponse getProcedures();

    ApiResponse getProcedureById(Long procedureId);

    ApiResponse updateProcedure(
            Long procedureId,
            ProcedureRequest request
    );

    ApiResponse updateProcedureStatus(
            Long procedureId,
            Boolean active
    );

    ApiResponse deleteProcedure(Long procedureId);

    ApiResponse searchProcedures(String keyword);

    // Treatment code


    ApiResponse createTreatment(PatientTreatmentRequest request);

    ApiResponse getTreatmentById(Long treatmentId);

    ApiResponse updateTreatment(
            Long treatmentId,
            PatientTreatmentRequest request
    );

    ApiResponse deleteTreatment(Long treatmentId);

    ApiResponse getTreatmentsByAdmission(Long admissionId);

    ApiResponse getTreatmentsByDoctor(Long doctorId);

    ApiResponse getTreatmentsByProcedure(Long procedureId);
}
