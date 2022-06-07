package com.mahama.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class FieldUtil {
    public static Object getProperty(Object bean, String propertyName) {
        Method getter = getMethod(bean, propertyName, "get", 0);
        if (getter != null) {
            try {
                return getter.invoke(bean);
            } catch (Exception ignored) {
            }
        }
        Field field = getField(bean, propertyName);
        if (field != null) {
            try {
                field.setAccessible(true);
                return field.get(bean);
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    public static <V> void setProperty(Object bean, String propertyName, V value) {
        Method method = getMethod(bean, propertyName, "set", 1);
        if (method != null) {
            try {
                method.invoke(bean, value);
            } catch (Exception ignored) {
            }
        }
    }

    public static Field getField(Object bean, String propertyName) {
        for (Field f : bean.getClass().getDeclaredFields()) {
            if (propertyName.equals(f.getName())) {
                return f;
            }
        }
        return null;
    }

    public static Method getMethod(Object bean, String propertyName, String pre, int paramLength) {
        StringBuilder sb = new StringBuilder(pre).append(Character.toUpperCase(propertyName.charAt(0)));
        if (propertyName.length() > 1) {
            sb.append(propertyName.substring(1));
        }
        String getterName = sb.toString();
        for (Method m : bean.getClass().getMethods()) {
            if (getterName.equals(m.getName()) && m.getParameterTypes().length == paramLength) {
                return m;
            }
        }
        return null;
    }
}
