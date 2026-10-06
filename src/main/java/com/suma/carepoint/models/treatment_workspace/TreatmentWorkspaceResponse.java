package com.suma.carepoint.models.treatment_workspace;
import com.suma.carepoint.models.medication.PrescriptionResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TreatmentWorkspaceResponse {

    private TreatmentWorkspacePatientResponse patient;

    private TreatmentWorkspaceAdmissionResponse admission;

    private List<TreatmentWorkspaceTreatmentResponse> treatments;

    private List<PrescriptionResponse> prescriptions;
}
