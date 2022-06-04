package com.mahama.common.exception;

public class TokenInvalidException extends RuntimeException {
    public TokenInvalidException() {
        super();
    }

    public TokenInvalidException(String msg) {
        super(msg);
    }

    public TokenInvalidException(String msg, Throwable e) {
        super(msg, e);
    }
}
