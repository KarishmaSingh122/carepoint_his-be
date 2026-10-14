package com.suma.carepoint.controllers;

import com.suma.carepoint.models.ApiResponse;
import com.suma.carepoint.models.constants.ApiConstant;
import com.suma.carepoint.models.medical_service.CreateServiceCategoryRequest;
import com.suma.carepoint.models.medical_service.CreateServiceRequest;
import com.suma.carepoint.models.medical_service.ServiceCategoryResponse;
import com.suma.carepoint.models.medical_service.ServiceResponse;
import com.suma.carepoint.services.medical_service.ServiceService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiConstant.Controller.HOSPITAL_SERVICE)
@AllArgsConstructor
public class ServiceController {
    @Autowired
    private final ServiceService serviceService;

    @PostMapping(ApiConstant.HosptalService.SERVICE_CATEGORY)
    public ResponseEntity<ApiResponse> createServiceCategory(@RequestBody CreateServiceCategoryRequest request) {
        ServiceCategoryResponse serviceCategoryResponse = serviceService.createServiceCategory( request);
        return ResponseEntity.ok().body(new ApiResponse(1, "", serviceCategoryResponse));

    }

    @GetMapping(ApiConstant.HosptalService.SERVICE_CATEGORY)
    public ResponseEntity<ApiResponse> getAllServiceCategories() {
        List<ServiceCategoryResponse> serviceCategoryResponseList = serviceService.getAllServiceCategories();
        return ResponseEntity.ok().body(new ApiResponse(1, "", serviceCategoryResponseList));
    }

    @GetMapping(ApiConstant.HosptalService.SERVICE_CATEGORY+"/get/{serviceCategoryId}")
    public ResponseEntity<ApiResponse> getServiceCategoryByServiceCategoryId(@PathVariable(name = "serviceCategoryId") Long serviceCategoryId) {
        ServiceCategoryResponse serviceCategoryResponse = serviceService.getServiceCategoryByServiceCategoryId(serviceCategoryId);
        return ResponseEntity.ok().body(new ApiResponse(1, "", serviceCategoryResponse));

    }


    @PostMapping(ApiConstant.HosptalService.SERVICE)
    public ResponseEntity<ApiResponse> createService(@RequestBody CreateServiceRequest request) {
        ServiceResponse serviceResponse = serviceService.createService( request);
        return ResponseEntity.ok().body(new ApiResponse(1, "", serviceResponse));

    }

    @GetMapping(ApiConstant.HosptalService.SERVICE)
    public ResponseEntity<ApiResponse> getAllServices() {
        List<ServiceResponse> serviceResponseList = serviceService.getAllServices();
        return ResponseEntity.ok().body(new ApiResponse(1, "", serviceResponseList));
    }

    @GetMapping(ApiConstant.HosptalService.SERVICE_CATEGORY+"/get/{serviceId}")
    public ResponseEntity<ApiResponse> getServiceByServiceId(@PathVariable(name = "serviceId") Long serviceId) {
        ServiceResponse serviceResponse = serviceService.getServiceByServiceId(serviceId);
        return ResponseEntity.ok().body(new ApiResponse(1, "", serviceResponse));

    }

    @GetMapping(ApiConstant.HosptalService.SERVICE_CATEGORY+"/get/by-service-code/{serviceCode}")
    public ResponseEntity<ApiResponse> getServiceByServiceCode(@PathVariable(name = "serviceCode") String serviceCode) {
        ServiceResponse serviceResponse = serviceService.getServiceByServiceCode(serviceCode);
        return ResponseEntity.ok().body(new ApiResponse(1, "", serviceResponse));
    }
    
}
