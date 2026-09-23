package com.suma.carepoint.services.visit;

import com.suma.carepoint.models.utility.PageResponse;
import com.suma.carepoint.models.visit.VisitRequest;
import com.suma.carepoint.models.visit.VisitResponse;

public interface VisitService {

    VisitResponse create(VisitRequest request);

    VisitResponse getById(Long visitId);

    PageResponse getAll(Long patientId, Long doctorId,
                        Long departmentId, String status, int page, int size);

    VisitResponse update(Long visitId, VisitRequest request);

    VisitResponse updateStatus(Long visitId, String request);
}