package com.mahama.common.exception;

public class ApplicationException  extends RuntimeException {
    public ApplicationException(ServiceExceptionEnum serviceExceptionEnum) {
        super(serviceExceptionEnum.getMessage());
    }

    public ApplicationException(String message) {
        super(message);
    }
}