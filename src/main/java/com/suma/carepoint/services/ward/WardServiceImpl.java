package com.suma.carepoint.services.ward;

import com.suma.carepoint.entities.floor.Floor;
import com.suma.carepoint.entities.organization.Department;
import com.suma.carepoint.entities.ward.Ward;
import com.suma.carepoint.models.ApiResponse;
import com.suma.carepoint.models.ward.CreateWardRequest;
import com.suma.carepoint.repositories.floor.FloorRepository;
import com.suma.carepoint.repositories.organization.DepartmentRepository;
import com.suma.carepoint.repositories.ward.WardRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
public class WardServiceImpl implements WardService{

    private final WardRepository wardRepository;
    private final DepartmentRepository departmentRepository;
    private final FloorRepository floorRepository;

    public WardServiceImpl(WardRepository wardRepository, DepartmentRepository departmentRepository ,
                           FloorRepository floorRepository) {
        this.wardRepository = wardRepository;
        this.departmentRepository = departmentRepository;
        this.floorRepository = floorRepository;
    }

    @Override
    public ApiResponse createWard(CreateWardRequest request) {

        try {

            Department department = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found"));
            Floor floor = floorRepository.findById(request.getFloorId())
                    .orElseThrow(() -> new RuntimeException("Floor not found"));
            Ward ward = new Ward();
            ward.setDepartment(department);
            ward.setFloor(floor);
            ward.setWardName(request.getWardName());
            ward.setWardType(request.getWardType());
            ward.setActive(request.getActive() != null ? request.getActive() : true);
            Ward savedWard = wardRepository.save(ward);
            return new ApiResponse(1, "", savedWard);

        }
        catch (Exception e) {
            log.error("Error occurred while creating ward", e);
            return new ApiResponse(2, "", null);
        }
    }



    @Override
    public ApiResponse getWardById(Long wardId) {

        try {
            Ward ward = wardRepository.findById(wardId).orElseThrow(() -> new RuntimeException("Ward not found"));
            return new ApiResponse(1, "", ward);

        }
        catch (Exception e) {
            log.error("Error occurred while getting ward", e);
            return new ApiResponse(2, "", null);
        }
    }


    @Override
    public ApiResponse getAllWards() {

        try {
            List<Ward> wards = wardRepository.findAll();
            return new ApiResponse(1, "", wards, (long) wards.size());

        } catch (Exception e) {
            log.error("Error occurred while getting all wards", e);
            return new ApiResponse(2, "", null);
        }
    }

    @Override
    public ApiResponse updateWard(Long wardId, CreateWardRequest request) {

        try {

            Ward ward = wardRepository.findById(wardId).orElseThrow(() -> new RuntimeException("Ward not found"));

            Department department = departmentRepository.findById(request.getDepartmentId()).orElseThrow(() ->
                            new RuntimeException("Department not found"));

            Floor floor = floorRepository.findById(request.getFloorId()).orElseThrow(() ->
                            new RuntimeException("Floor not found"));

            ward.setDepartment(department);
            ward.setFloor(floor);
            ward.setWardName(request.getWardName());
            ward.setWardType(request.getWardType());

            if (request.getActive() != null) {
                ward.setActive(request.getActive());
            }

            Ward updatedWard = wardRepository.save(ward);
            return new ApiResponse(1, "", updatedWard);

        } catch (Exception e) {

            log.error("Error occurred while updating ward", e);
            return new ApiResponse(2, "", null);
        }
    }

    @Override
    public ApiResponse deleteWard(Long wardId) {

        try {
            Ward ward = wardRepository.findById(wardId).orElseThrow(() ->
                            new RuntimeException("Ward not found"));

            wardRepository.delete(ward);
            return new ApiResponse(1, "Ward deleted successfully", null);

        } catch (Exception e) {
            log.error("Error occurred while deleting ward", e);
            return new ApiResponse(2, "", null);
        }
    }
}

