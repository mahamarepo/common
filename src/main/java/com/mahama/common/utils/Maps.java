package com.mahama.common.utils;


import java.lang.reflect.Field;
import java.util.*;

/**
 * Map 工具类
 */
public final class Maps {

    private Maps() {
    }

    public static <K, V> HashMap<K, V> newHashMap() {
        return new HashMap<K, V>(100);
    }

    public static <K, V> HashMap<K, V> newHashMap(K k, V v) {
        HashMap<K, V> map = new HashMap<K, V>(100);
        map.put(k, v);
        return map;
    }

    @SuppressWarnings("unchecked")
    public static <K, V> HashMap<K, V> newHashMap(K k, V v,
                                                  Object... extraKeyValues) {
        if (extraKeyValues.length % 2 != 0) {
            throw new IllegalArgumentException();
        }
        HashMap<K, V> map = new HashMap<K, V>(100);
        map.put(k, v);
        for (int i = 0; i < extraKeyValues.length; i += 2) {
            k = (K) extraKeyValues[i];
            v = (V) extraKeyValues[i + 1];
            map.put(k, v);
        }
        return map;
    }

    /**
     * 将Object对象里面的属性和值转化成Map对象
     *
     * @param obj
     * @return
     * @throws IllegalAccessException
     */
    public static <M> Map<String, Object> objectToMap(M obj) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<String,Object>();
        Class<?> clazz = obj.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object value = field.get(obj);
            map.put(fieldName, value);
        }
        return map;
    }


    public static <K, V> Map<V, V> toMap(List<Map<K, V>> list, K keyProperty, K valueProperty) {
        Map<V, V> map = new HashMap<>();
        for (Map<K, V> m : list) {

            try {
                V k = m.get(keyProperty);
                V v = m.get(valueProperty);
                map.put(k, v);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return map;
    }

    public static <K, V> Map<K, V> filter(Map<K, V> kvMap,String regex){
        return com.google.common.collect.Maps.filterEntries(kvMap,map -> map.getKey().toString().matches(regex));
    }
}
