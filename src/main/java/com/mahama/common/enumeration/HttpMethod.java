package com.mahama.common.enumeration;

public enum HttpMethod {
    GET("GET"),
    POST("POST"),
    PUT("PUT");


    private String value;

    HttpMethod(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
