package com.suma.carepoint.repositories.medical_service;

import com.suma.carepoint.entities.medical_service.HospitalService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<HospitalService,Long> {
    Optional<HospitalService> findByServiceCode(String serviceCode);

    List<HospitalService> findByActiveTrue();

    List<HospitalService> findByServiceCategoryServiceCategoryId(Long serviceCategoryId);
}
