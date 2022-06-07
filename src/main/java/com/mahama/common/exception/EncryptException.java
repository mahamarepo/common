package com.mahama.common.exception;

/**
 * 加密异常
 */
public class EncryptException extends RuntimeException {
    public EncryptException(ServiceExceptionEnum serviceExceptionEnum) {
        super(serviceExceptionEnum.getMessage());
    }

    public EncryptException(String message) {
        super(message);
    }
}
