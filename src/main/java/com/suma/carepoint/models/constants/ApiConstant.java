package com.suma.carepoint.models.constants;

public class ApiConstant {

    private ApiConstant() {
    }

    public static class Controller {

        private Controller() {
        }

         public static final String HMIS = "api/hmis";
         public static final String BILL = "api/billing";
         public static final String HOSPITAL_SERVICE = "api/services";
        public static final String MEDICATION = "api/medication";
        public static final String PROCEDURE = "api/procedure";
        public static final String DIAGNOSIS="api/diagnosis";




    }
    public static class Hmis {
        private Hmis() {}
    }

    public static class Patient {
        private Patient() {}
        public static final String GET = "/get";
        public static final String GET_BY_PATIENT_ID = "/get-patient-id";
        public static final String GET_BY_ABHA_ID = "/get-abha-id";

        public static final String CREATE ="/create";
        public static final String UPDATE ="/update";
        public static final String DELETE ="/delete";

    }

    public static class Bill{
        private Bill(){}

        public static final String BILLS = "/bills";
        public static final String BILLS_BY_NUMBER = "/bills/by-number";

    }

    public static final class Department {
        private Department() {}

        public static final String BASE = "/api/departments";
        public static final String CREATE = BASE;
        public static final String GET_BY_ID = BASE + "/{departmentId}";
        public static final String GET_ALL = BASE;
        public static final String UPDATE = BASE + "/{departmentId}";
        public static final String UPDATE_STATUS = BASE + "/{departmentId}/status";
        public static final String DELETE = BASE + "/{departmentId}";
        public static final String GET_ACTIVE = BASE + "/active";
    }

    public static final class Regexp {
        private Regexp() {}

        public static final String ALPHANUMERIC_DASH_UNDERSCORE = "^[A-Za-z0-9_-]+$";
        public static final String ALPHA_ONLY = "^[A-Za-z]+$";
        public static final String NUMERIC_ONLY = "^[0-9]+$";
        public static final String CONTACT_NUMBER = "^[0-9+() .-]*$";

    }


    public static class HosptalService{
        private HosptalService(){}

        public static final String SERVICE = "/service";
        public static final String SERVICE_CATEGORY = "/service/service-category";



    }

    public static class Admission{
        private Admission (){}
        public static final String CREATE = "/create";
        public static final String GET_BY_ID = "/get-by-id";
        public static final String GET_ALL = "/get-all";
        public static final String DELETE = "/delete";
        public static final String UPDATE = "/update";

    }

    public static final class Staff {
        private Staff() {}

        public static final String BASE = "/api/staff";
        public static final String CREATE = "";
        public static final String GET_BY_ID = "/{staffId}";
        public static final String GET_ALL = "";
        public static final String GET_ACTIVE = "/active";
        public static final String GET_BY_DEPARTMENT = "/department";
        public static final String GET_BY_DESIGNATION = "/designation";
        public static final String UPDATE = "/{staffId}";
        public static final String UPDATE_STATUS = "/{staffId}/status";
        public static final String DELETE = "/{staffId}";
    }

    public static class Medication{
        private Medication(){}

        public static final String MEDICATIONS = "/medications";

        public static final String PRESCRIPTION = "/prescription";
        public static final String PATIENT_PRESCRIPTIONS = "/prescription/patient";
        public static final String VISIT_PRESCRIPTIONS = "/prescription/visit";
        public static final String DOCTOR_PRESCRIPTIONS = "/prescription/doctor";


    }


    public static final class Visit {
        private Visit() {}

        public static final String BASE = "/api/visits";
        public static final String CREATE = "";
        public static final String GET_BY_ID = "/{visitId}";
        public static final String GET_ALL = "";
        public static final String UPDATE = "/{visitId}";
        public static final String STATUS = "/{visitId}/status";
    }

    public static class Floor {
        private Floor(){}
        public static final String CREATE = "/floor/create";
        public static final String GET_ALL = "/floor/get-all";
        public static final String GET_BY_ID = "/floor/get-by-id";
        public static final String UPDATE = "/floor/update";
        public static final String DELETE = "/floor/delete";
    }


    public static class Ward{
        private Ward(){}
        public static final String CREATE = "/ward/create";
        public static final String GET_ALL = "/ward/get-all";
        public static final String GET_BY_ID = "/ward/get-by-id";
        public static final String UPDATE = "/ward/update";
        public static final String DELETE = "/ward/delete";
    }



    public static class Procedure{
        private Procedure(){}

        public static final String PROCEDURE = "/procedure";
        public static final String TREATMENT = "/treatment";
        public static final String PATIENT_TREATMENT = "/treatment/patient";
        public static final String ADMISSION_TREATMENTS ="/treatment/admission";
        public static final String DOCTOR_TREATMENTS ="/treatment/doctor";
        public static final String PROCEDURE_TREATMENTS ="/treatment/procedure";

    }

    public static class Diagnosis{
        private Diagnosis(){}
        public static final String DIAGNOSIS ="/diagnosis";
    }

}