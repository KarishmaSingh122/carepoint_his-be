package com.suma.carepoint.entities.document;

public enum DocumentType {
    PHOTO,
    PAN,
    AADHAAR,
    PASSPORT,
    DRIVING_LICENSE,
    VOTER_ID,
    INSURANCE,
    MEDICAL_REPORT,
    PRESCRIPTION,
    DISCHARGE_SUMMARY,
    LAB_REPORT,
    OTHER;

    public static DocumentType fromValue(String value) {
        return DocumentType.valueOf(value.toUpperCase());
    }
}
