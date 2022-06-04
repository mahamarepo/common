package com.mahama.common.exception;

public class RequestLimitException extends RuntimeException {
    public RequestLimitException(String msg){
        super(msg);
    }
}
