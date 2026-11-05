package com.suma.carepoint.repositories.ward;

import com.suma.carepoint.entities.ward.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WardRepository extends JpaRepository<Ward,Long> {

    List<Ward> findByDepartmentDepartmentId(Long departmentId);

    List<Ward> findByFloorFloorId(Long floorId);

    List<Ward> findByActiveTrue();

    @Query("SELECT w FROM Ward w WHERE w.floor.floorName = :floorName")
    List<Ward> findByFloorName(@Param("floorName") String floorName);
}
