package com.mahama.common.exception;

/**
 * 封装断言的异常
 */
public class AssertionException extends AssertionError {
    public AssertionException(String msg) {
        super(msg);
    }

    public AssertionException(ServiceExceptionEnum serviceExceptionEnum) {
        super(serviceExceptionEnum.getMessage());
    }
}
