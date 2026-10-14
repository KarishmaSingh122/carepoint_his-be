package com.suma.carepoint.repositories.medical_service;

import com.suma.carepoint.entities.medical_service.ServiceCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceCategoryRepository extends JpaRepository<ServiceCategory,Long> {
    Optional<ServiceCategory> findByCategoryName(String categoryName);

    List<ServiceCategory> findByActiveTrue();
}
