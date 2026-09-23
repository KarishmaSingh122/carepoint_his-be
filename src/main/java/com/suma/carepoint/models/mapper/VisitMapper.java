package com.suma.carepoint.models.mapper;


import com.suma.carepoint.entities.visit.Visit;
import com.suma.carepoint.models.visit.VisitRequest;
import com.suma.carepoint.models.visit.VisitResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VisitMapper {

    private final ModelMapper modelMapper;

    public VisitResponse toResponse(Visit visit) {
        VisitResponse response =
                modelMapper.map(
                        visit,
                        VisitResponse.class
                );

        if (visit.getPatient() != null) {
            response.setPatientId(
                    visit.getPatient().getPatientId()
            );
        }

        if (visit.getDoctor() != null) {
            response.setDoctorId(
                    visit.getDoctor().getStaffId()
            );
            response.setDoctorEmployeeNo(
                    visit.getDoctor().getEmployeeNo()
            );
            response.setDoctorFirstName(
                    visit.getDoctor().getFirstName()
            );
            response.setDoctorLastName(
                    visit.getDoctor().getLastName()
            );
        }

        if (visit.getDepartment() != null) {
            response.setDepartmentId(
                    visit.getDepartment().getDepartmentId()
            );
            response.setDepartmentCode(
                    visit.getDepartment().getDepartmentCode()
            );
            response.setDepartmentName(
                    visit.getDepartment().getDepartmentName()
            );
        }

        return response;
    }

    public Visit toEntity(VisitRequest request) {
        Visit visit = modelMapper.map(
                request,
                Visit.class
        );

        visit.setPatient(null);
        visit.setDoctor(null);
        visit.setDepartment(null);

        return visit;
    }
}
