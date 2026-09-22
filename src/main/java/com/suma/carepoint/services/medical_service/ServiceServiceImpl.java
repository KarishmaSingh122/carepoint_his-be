package com.suma.carepoint.services.medical_service;

import com.suma.carepoint.entities.medical_service.HospitalService;
import com.suma.carepoint.entities.medical_service.ServiceCategory;
import com.suma.carepoint.models.medical_service.CreateServiceCategoryRequest;
import com.suma.carepoint.models.medical_service.CreateServiceRequest;
import com.suma.carepoint.models.medical_service.ServiceCategoryResponse;
import com.suma.carepoint.models.medical_service.ServiceResponse;
import com.suma.carepoint.repositories.medical_service.ServiceCategoryRepository;
import com.suma.carepoint.repositories.medical_service.ServiceRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ServiceServiceImpl implements ServiceService{

    private final ServiceRepository serviceRepository;
    private final ServiceCategoryRepository serviceCategoryRepository;

    private final ModelMapper modelMapper;

    public ServiceServiceImpl(ModelMapper modelMapper,
                              ServiceRepository serviceRepository,
                              ServiceCategoryRepository serviceCategoryRepository){
        this.modelMapper= modelMapper;
        this.serviceRepository = serviceRepository;
        this.serviceCategoryRepository= serviceCategoryRepository;
    }

    // ============================================================
    // CREATE SERVICE CATEGORY
    // ============================================================

    @Override
    public ServiceCategoryResponse createServiceCategory(
            CreateServiceCategoryRequest request) {

        log.info(
                "Creating service category: {}",
                request.getCategoryName()
        );

        if (request.getCategoryName() == null ||
                request.getCategoryName().isBlank()) {

            throw new IllegalArgumentException(
                    "Category name is required"
            );
        }

        // Check duplicate category
        if (serviceCategoryRepository
                .findByCategoryName(request.getCategoryName())
                .isPresent()) {

            throw new IllegalArgumentException(
                    "Service category already exists: "
                            + request.getCategoryName()
            );
        }

        ServiceCategory category =
                modelMapper.map(
                        request,
                        ServiceCategory.class
                );

        // New category should be active
        category.setActive(true);

        ServiceCategory savedCategory =
                serviceCategoryRepository.save(category);

        log.info(
                "Service category created successfully. id={}",
                savedCategory.getServiceCategoryId()
        );

        return modelMapper.map(
                savedCategory,
                ServiceCategoryResponse.class
        );
    }

    // ============================================================
    // GET SERVICE CATEGORY BY ID
    // ============================================================

    @Override
    public ServiceCategoryResponse getServiceCategoryByServiceCategoryId(
            Long serviceCategoryId) {

        log.info(
                "Fetching service category. id={}",
                serviceCategoryId
        );

        ServiceCategory category =
                serviceCategoryRepository
                        .findById(serviceCategoryId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Service category not found with id: "
                                                + serviceCategoryId
                                )
                        );

        return modelMapper.map(
                category,
                ServiceCategoryResponse.class
        );
    }

    // ============================================================
    // CREATE SERVICE
    // ============================================================

    @Override
    public ServiceResponse createService(
            CreateServiceRequest request) {

        log.info(
                "Creating service. code={}",
                request.getServiceCode()
        );

        if (request.getServiceCode() == null ||
                request.getServiceCode().isBlank()) {

            throw new IllegalArgumentException(
                    "Service code is required"
            );
        }

        if (request.getServiceName() == null ||
                request.getServiceName().isBlank()) {

            throw new IllegalArgumentException(
                    "Service name is required"
            );
        }

        // Check duplicate service code
        if (serviceRepository
                .findByServiceCode(request.getServiceCode())
                .isPresent()) {

            throw new IllegalArgumentException(
                    "Service already exists with code: "
                            + request.getServiceCode()
            );
        }

        // --------------------------------------------------------
        // Find Service Category
        // --------------------------------------------------------

        ServiceCategory category =
                serviceCategoryRepository
                        .findById(request.getServiceCategoryId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Service category not found with id: "
                                                + request.getServiceCategoryId()
                                )
                        );

        // --------------------------------------------------------
        // Map request → entity
        // --------------------------------------------------------

        HospitalService service =
                modelMapper.map(
                        request,
                        HospitalService.class
                );

        service.setServiceCategory(category);

        // New service should be active
        service.setActive(true);

        // --------------------------------------------------------
        // Save
        // --------------------------------------------------------

        HospitalService savedService =
                serviceRepository.save(service);

        log.info(
                "Service created successfully. id={}",
                savedService.getServiceId()
        );

        // --------------------------------------------------------
        // Entity → Response
        // --------------------------------------------------------

        ServiceResponse response =
                modelMapper.map(
                        savedService,
                        ServiceResponse.class
                );

        /*
         * If ServiceResponse contains category fields,
         * populate them explicitly.
         */
        if (savedService.getServiceCategory() != null) {

            response.setServiceCategoryId(
                    savedService
                            .getServiceCategory()
                            .getServiceCategoryId()
            );

            response.setServiceCategoryName(
                    savedService
                            .getServiceCategory()
                            .getCategoryName()
            );
        }

        return response;
    }

    // ============================================================
    // GET SERVICE BY ID
    // ============================================================

    @Override
    public ServiceResponse getServiceByServiceId(
            Long serviceId) {

        log.info(
                "Fetching service. id={}",
                serviceId
        );

        HospitalService service =
                serviceRepository
                        .findById(serviceId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Service not found with id: "
                                                + serviceId
                                )
                        );

        return mapServiceResponse(service);
    }

    // ============================================================
    // GET SERVICE BY CODE
    // ============================================================

    @Override
    public ServiceResponse getServiceByServiceCode(
            String serviceCode) {

        log.info(
                "Fetching service. code={}",
                serviceCode
        );

        if (serviceCode == null ||
                serviceCode.isBlank()) {

            throw new IllegalArgumentException(
                    "Service code is required"
            );
        }

        HospitalService service =
                serviceRepository
                        .findByServiceCode(serviceCode)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Service not found with code: "
                                                + serviceCode
                                )
                        );

        return mapServiceResponse(service);
    }

    // ============================================================
    // GET ALL SERVICE CATEGORIES
    // ============================================================

    @Override
    public List<ServiceCategoryResponse> getAllServiceCategories() {

        log.info("Fetching all service categories");

        return serviceCategoryRepository
                .findAll()
                .stream()
                .map(category ->
                        modelMapper.map(
                                category,
                                ServiceCategoryResponse.class
                        )
                )
                .toList();
    }

    // ============================================================
    // GET ALL SERVICES
    // ============================================================

    @Override
    public List<ServiceResponse> getAllServices() {

        log.info("Fetching all services");

        return serviceRepository
                .findAll()
                .stream()
                .map(this::mapServiceResponse)
                .toList();
    }

    // ============================================================
    // COMMON SERVICE → RESPONSE MAPPER
    // ============================================================

    private ServiceResponse mapServiceResponse(
            HospitalService service) {

        ServiceResponse response =
                modelMapper.map(
                        service,
                        ServiceResponse.class
                );

        if (service.getServiceCategory() != null) {

            response.setServiceCategoryId(
                    service.getServiceCategory()
                            .getServiceCategoryId()
            );

            response.setServiceCategoryName(
                    service.getServiceCategory()
                            .getCategoryName()
            );
        }

        return response;
    }
}