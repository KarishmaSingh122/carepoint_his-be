package com.suma.carepoint.models.mapper;

import com.suma.carepoint.entities.department.Department;
import com.suma.carepoint.models.department.DepartmentRequest;
import com.suma.carepoint.models.department.DepartmentResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DepartmentMapper {

    private final ModelMapper modelMapper;

    public Department toEntity(DepartmentRequest request) {
        return modelMapper.map(request, Department.class);
    }

    public DepartmentResponse toResponse(Department department) {
        return modelMapper.map(department, DepartmentResponse.class);
    }

    public void updateEntity(DepartmentRequest request, Department department) {
        modelMapper.map(request, department);
    }
}
