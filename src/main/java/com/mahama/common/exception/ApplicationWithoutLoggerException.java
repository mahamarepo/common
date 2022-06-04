package com.mahama.common.exception;

/**
 * 封装系统的异常
 */
public class ApplicationWithoutLoggerException extends RuntimeException {
    public ApplicationWithoutLoggerException(ServiceExceptionEnum serviceExceptionEnum) {
        super(serviceExceptionEnum.getMessage());
    }

    public ApplicationWithoutLoggerException(String message) {
        super(message);
    }
}
