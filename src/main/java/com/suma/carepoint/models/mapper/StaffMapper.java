package com.suma.carepoint.models.mapper;

import com.suma.carepoint.entities.organization.Staff;
import com.suma.carepoint.models.organization.StaffRequest;
import com.suma.carepoint.models.organization.StaffResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StaffMapper {

    private final ModelMapper modelMapper;

    public Staff toEntity(StaffRequest request) {
        return modelMapper.map(request, Staff.class);
    }

    public StaffResponse toResponse(Staff staff) {
        return modelMapper.map(staff, StaffResponse.class);
    }

    public void updateEntity(StaffRequest request, Staff staff) {
        modelMapper.map(request, staff);
    }
}
