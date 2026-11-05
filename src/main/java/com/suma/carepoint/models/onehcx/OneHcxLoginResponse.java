package com.suma.carepoint.models.onehcx;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OneHcxLoginResponse(int status, String message, Data data) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Data(String token, String type) {
    }
}
