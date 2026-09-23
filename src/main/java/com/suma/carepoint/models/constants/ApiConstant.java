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


}