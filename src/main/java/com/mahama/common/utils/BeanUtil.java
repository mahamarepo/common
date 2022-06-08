package com.mahama.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.cglib.beans.BeanMap;

import java.util.*;

@Slf4j
public class BeanUtil {
    /**
     * 将List<T>转换为List<Map<String, Object>>
     */
    public static <T> List<Map<String, Object>> objectsToMaps(List<T> objList) {
        List<Map<String, Object>> list = new ArrayList<>();
        if (objList != null && objList.size() > 0) {
            Map<String, Object> map;
            T bean;
            for (T t : objList) {
                bean = t;
                map = beanToMap(bean);
                list.add(map);
            }
        }
        return list;
    }

    /**
     * 将对象装换为map
     */
    public static <T> Map<String, Object> beanToMap(T bean) {
        Map<String, Object> map = Maps.newHashMap();
        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);
            for (Object key : beanMap.keySet()) {
                map.put(key + "", beanMap.get(key));
            }
        }
        return map;
    }

    /**
     * 对象组中是否存在 Empty Object
     */
    public static boolean isOneEmpty(Object... os) {
        for (Object o : os) {
            if (StringUtil.isNullOrEmpty(o)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 映射除空字符和NULL值的属性
     *
     * @param src    源对象
     * @param target 目标对象
     */
    public static <M, T> T copyPropertiesWithOutNull(M src, T target) {
        BeanUtils.copyProperties(src, target, getNullOrEmptyPropertyNames(src));
        return target;
    }

    /**
     * 映射属性
     *
     * @param src    源对象
     * @param target 目标对象
     */
    public static <M, T> T copyProperties(M src, T target) {
        BeanUtils.copyProperties(src, target);
        return target;
    }

    /**
     * 映射属性
     *
     * @param src   源对象
     * @param clazz 目标类
     */
    public static <M, T> T copyProperties(M src, Class<T> clazz) {
        T target = instantiateClass(clazz);
        return copyProperties(src, target);
    }

    /**
     * 映射属性
     *
     * @param src   源对象列表
     * @param clazz 目标类
     */
    public static <M, T> List<T> copyProperties(List<M> src, Class<T> clazz) {
        List<T> list = new ArrayList<>();
        src.forEach(s -> {
            T target = instantiateClass(clazz);
            BeanUtils.copyProperties(s, target);
            list.add(target);
        });
        return list;
    }

    public interface CopyListTask<M, T> {
        void execute(M m, T t);
    }

    public interface CopyListFilter<M> {
        boolean execute(M m);
    }

    /**
     * 映射属性
     *
     * @param src          源对象列表
     * @param clazz        目标类
     * @param copyListTask 映射时对目标对象的额外操作
     */
    public static <M, T> List<T> copyProperties(List<M> src, Class<T> clazz, CopyListTask<M, T> copyListTask) {
        return copyProperties(src, clazz, copyListTask, m -> true);
    }

    /**
     * 映射属性
     *
     * @param src    源对象列表
     * @param clazz  目标类
     * @param filter 映射过滤
     */
    public static <M, T> List<T> copyProperties(List<M> src, Class<T> clazz, CopyListFilter<M> filter) {
        return copyProperties(src, clazz, (m, t) -> {
        }, filter);
    }

    /**
     * 映射属性
     *
     * @param src          源对象列表
     * @param clazz        目标类
     * @param copyListTask 映射时对目标对象的额外操作
     * @param filter       映射过滤
     */
    public static <M, T> List<T> copyProperties(List<M> src, Class<T> clazz, CopyListTask<M, T> copyListTask, CopyListFilter<M> filter) {
        List<T> list = new ArrayList<>();
        src.forEach(s -> {
            if (filter.execute(s)) {
                T target = instantiateClass(clazz);
                BeanUtils.copyProperties(s, target);
                copyListTask.execute(s, target);
                list.add(target);
            }
        });
        return list;
    }

    /**
     * 初始化类，适用于泛型类
     */
    public static <T> T instantiateClass(Class<T> clazz) {
        return BeanUtils.instantiateClass(clazz);
    }

    /**
     * 获取对象中null值和空字符串的属性列表
     */
    private static String[] getNullOrEmptyPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (StringUtil.isNullOrEmpty(srcValue)) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
}

