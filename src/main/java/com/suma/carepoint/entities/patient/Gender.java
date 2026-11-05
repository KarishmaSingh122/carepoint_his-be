package com.suma.carepoint.entities.patient;

public enum Gender {
    MALE,
    FEMALE,
    OTHER;

    public String toCamel() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}
