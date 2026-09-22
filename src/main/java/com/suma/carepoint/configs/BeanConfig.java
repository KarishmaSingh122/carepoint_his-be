package com.suma.carepoint.configs;

import com.suma.carepoint.entities.bill.Bill;
import com.suma.carepoint.entities.medical_service.HospitalService;
import com.suma.carepoint.entities.patient.Patient;
import com.suma.carepoint.models.billing.BillResponse;
import com.suma.carepoint.models.medical_service.CreateServiceRequest;
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
                        mapper.skip(Patient::setPatientId);
                    });

            // CreateServiceRequest -> Service
            // Skip generated Service ID
            modelMapper.typeMap(CreateServiceRequest.class, HospitalService.class)
                    .addMappings(mapper -> {
                        mapper.skip(HospitalService::setServiceId);
                    });

//            modelMapper.typeMap(Bill.class, BillResponse.class)
//                    .addMappings(mapper -> {
//                        mapper.skip(BillResponse::setPatientId);
////                        mapper.skip(BillResponse::setVisitId);
////                        mapper.skip(BillResponse::setAdmissionId);
//                        mapper.skip(BillResponse::setPaidAmount);
//                        mapper.skip(BillResponse::setBalanceAmount);
//                        mapper.skip(BillResponse::setItems);
//                        mapper.skip(BillResponse::setPayments);
//                    });

            return modelMapper;
        }


}
