package com.mahama.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FunctionalInterface
public interface ApplicationExceptionFunction {
    void throwMessage(String message);

    default ApplicationExceptionFunction error(ApplicationExceptionFunction this, Class<?> clazz, String ...message) {
        Logger logger = LoggerFactory.getLogger(clazz);
        logger.error(String.join(" ",message));
        return this;
    }
}
