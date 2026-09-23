package com.suma.carepoint.repositories.ward;

import com.suma.carepoint.entities.ward.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WardRepository extends JpaRepository<Ward,Long> {

    List<Ward> findByDepartmentDepartmentId(Long departmentId);

    List<Ward> findByFloorFloorId(Long floorId);

    List<Ward> findByActiveTrue();
}
