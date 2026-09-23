package com.suma.carepoint.configs;

import com.suma.carepoint.entities.medical_service.HospitalService;
import com.suma.carepoint.entities.medication.Prescription;
import com.suma.carepoint.entities.organization.Staff;
import com.suma.carepoint.entities.patient.Patient;
import com.suma.carepoint.models.medical_service.CreateServiceRequest;
import com.suma.carepoint.models.medication.PrescriptionResponse;
import com.suma.carepoint.models.organization.StaffRequest;
import com.suma.carepoint.models.patient.CreatePatientRequest;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class BeanConfig {

        @Bean
        public ModelMapper modelMapper() {

            ModelMapper modelMapper =  new ModelMapper();

            modelMapper.getConfiguration()
                    .setMatchingStrategy(MatchingStrategies.STRICT);
            // Only BillingDto -> Billing
            modelMapper.typeMap(CreatePatientRequest.class, Patient.class)
                    .addMappings(mapper -> {
                        mapper.skip(Patient::setPatientId);
                    });

            // CreateServiceRequest -> Service
            // Skip generated Service ID
            modelMapper.typeMap(CreateServiceRequest.class, HospitalService.class)
                    .addMappings(mapper -> {
                        mapper.skip(HospitalService::setServiceId);
                    });

            //Prescription -> PrescriptionResponse
            modelMapper.typeMap(
                    Prescription.class,
                    PrescriptionResponse.class
            ).addMappings(mapper -> {

                mapper.skip(PrescriptionResponse::setPatientId);

//                mapper.skip(PrescriptionResponse::setDoctorId);
//
//                mapper.skip(PrescriptionResponse::setVisitId);

                mapper.skip(PrescriptionResponse::setItems);
            });

            modelMapper.typeMap(StaffRequest.class, Staff.class)
                    .addMappings(mapper -> mapper.skip(Staff::setStaffId));
            modelMapper.getConfiguration()
                    .setMatchingStrategy(MatchingStrategies.STRICT);

            return modelMapper;
        }


}
