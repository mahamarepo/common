package com.mahama.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 映射类操作
 */
@Slf4j
public class ReflectionUtil {
    /**
     * 获取泛型对象真实CLASS
     * @param clazz 当前类
     * @param index 类中泛型对象序号
     * @return 泛型类真实类
     */
    public static <T> Class<T> getSuperClassGenericType(final Class<?> clazz, final int index) {
		Type genType = clazz.getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            return null;
        }
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        if (index >= params.length || index < 0) {
            return null;
        }
        if (!(params[index] instanceof Class)) {
            return null;
        }
        return (Class<T>) params[index];
    }
	
	public static <T> Class<T> getInterfacesGenericType(final Class<?> clazz, final int infIndex, final int index) {
		Type genType = clazz.getGenericInterfaces()[infIndex];
        if (!(genType instanceof ParameterizedType)) {
            return null;
        }
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        if (index >= params.length || index < 0) {
            return null;
        }
        if (!(params[index] instanceof Class)) {
            return null;
        }
        return (Class<T>) params[index];
    }

}
