package com.suma.carepoint.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponse {
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int status;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String message;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Object data;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Long totalRecords;

    public ApiResponse() {
    }

    public ApiResponse(String message) {
        this.message = message;
    }

    public ApiResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public ApiResponse(int status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public ApiResponse(int status, String message, Object data, Long totalRecords) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.totalRecords = totalRecords;
    }
}


