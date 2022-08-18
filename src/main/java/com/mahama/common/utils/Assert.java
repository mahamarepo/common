package com.mahama.common.utils;

import com.mahama.common.exception.AssertionExceptionFunction;
import com.mahama.common.exception.AssertionException;
import com.mahama.common.exception.ServiceExceptionEnum;

import java.util.List;

public class Assert {
    public interface MsgTask {
        String execute();
    }

    public static void err(String message) {
        throw new AssertionException(message);
    }

    public static void err(ServiceExceptionEnum exceptionEnum) {
        throw new AssertionException(exceptionEnum);
    }

    public static AssertionExceptionFunction isTrue(boolean value) {
        return (message) -> isTrue(value, message);
    }

    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            err(message);
        }
    }

    public static void isTrue(boolean expression, ServiceExceptionEnum exceptionEnum) {
        if (!expression) {
            err(exceptionEnum);
        }
    }

    public static void isTrue(boolean expression, MsgTask msgTask) {
        isTrue(expression, msgTask.execute());
    }

    public static AssertionExceptionFunction isFalse(boolean value) {
        return (message) -> isFalse(value, message);
    }

    public static void isFalse(boolean expression, String message) {
        isTrue(!expression, message);
    }

    public static void isFalse(boolean expression, ServiceExceptionEnum exceptionEnum) {
        isTrue(!expression, exceptionEnum);
    }

    public static void isFalse(boolean expression, MsgTask msgTask) {
        isTrue(!expression, msgTask.execute());
    }

    public static AssertionExceptionFunction isNull(Object object) {
        return (message) -> isNull(object, message);
    }

    public static void isNull(Object object, String message) {
        isTrue(object == null, message);
    }

    public static void isNull(Object object, ServiceExceptionEnum exceptionEnum) {
        isTrue(object == null, exceptionEnum);
    }

    public static void isNull(Object object, MsgTask msgTask) {
        isTrue(object == null, msgTask.execute());
    }

    public static AssertionExceptionFunction notNull(Object object) {
        return (message) -> notNull(object, message);
    }

    public static void notNull(Object object, String message) {
        isTrue(object != null, message);
    }

    public static void notNull(Object object, ServiceExceptionEnum exceptionEnum) {
        isTrue(object != null, exceptionEnum);
    }

    public static void notNull(Object object, MsgTask msgTask) {
        isTrue(object != null, msgTask.execute());
    }

    public static AssertionExceptionFunction isEmpty(String val) {
        return (message) -> isEmpty(val, message);
    }

    public static void isEmpty(String val, String message) {
        isTrue(StringUtil.isNullOrEmpty(val), message);
    }

    public static <T> void isEmpty(List<T> val, String message) {
        isTrue(val == null || val.size() == 0, message);
    }

    public static AssertionExceptionFunction notEmpty(String val) {
        return (message) -> notEmpty(val, message);
    }

    public static void notEmpty(String val, String message) {
        isTrue(StringUtil.isNotNullOrEmpty(val), message);
    }

    public static void notEmpty(String val, ServiceExceptionEnum exceptionEnum) {
        isTrue(StringUtil.isNotNullOrEmpty(val), exceptionEnum);
    }

    public static <T> void notEmpty(List<T> val, String message) {
        isTrue(val != null && val.size() > 0, message);
    }

    public static <T> void notEmpty(List<T> val, ServiceExceptionEnum exceptionEnum) {
        isTrue(val != null && val.size() > 0, exceptionEnum);
    }

}
