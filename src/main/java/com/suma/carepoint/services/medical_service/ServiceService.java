package com.suma.carepoint.services.medical_service;

import com.suma.carepoint.models.medical_service.CreateServiceCategoryRequest;
import com.suma.carepoint.models.medical_service.CreateServiceRequest;
import com.suma.carepoint.models.medical_service.ServiceCategoryResponse;
import com.suma.carepoint.models.medical_service.ServiceResponse;

import java.util.List;

public interface ServiceService {
    ServiceCategoryResponse createServiceCategory(CreateServiceCategoryRequest request);

    ServiceCategoryResponse getServiceCategoryByServiceCategoryId(Long serviceCategoryId);

    ServiceResponse createService(CreateServiceRequest request);

    ServiceResponse getServiceByServiceId(Long serviceId);

    ServiceResponse getServiceByServiceCode(String serviceCode);

    List<ServiceCategoryResponse> getAllServiceCategories();

    List<ServiceResponse> getAllServices();
}
