package com.suma.carepoint.configs;

import com.suma.carepoint.entities.patient.Patient;
import com.suma.carepoint.models.patient.CreatePatientRequest;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class BeanConfig {

        @Bean
        public ModelMapper modelMapper() {

            ModelMapper modelMapper =  new ModelMapper();
            // Only BillingDto -> Billing
            modelMapper.typeMap(CreatePatientRequest.class, Patient.class)
                    .addMappings(mapper -> {
                        mapper.skip(Patient::setId);
                    });
            return modelMapper;
        }


}
