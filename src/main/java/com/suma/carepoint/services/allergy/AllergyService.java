package com.suma.carepoint.services.allergy;

import com.suma.carepoint.models.ApiResponse;
import com.suma.carepoint.models.allergy.CreateAllergyRequest;

public interface AllergyService {

    ApiResponse createAllergy(CreateAllergyRequest request);

    ApiResponse getAllergy(Long allergyId);

    ApiResponse getAllAllergies();

    ApiResponse updateAllergy(Long allergyId, CreateAllergyRequest request);

    ApiResponse deleteAllergy(Long allergyId);
}
