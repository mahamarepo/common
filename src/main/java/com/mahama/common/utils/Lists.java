package com.mahama.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 集合工具类
 *
 * @author enilu
 */
public final class Lists {

    private Lists() {
    }


    /**
     * 碾平集合咯，主要针对集合元素为集合的情况有效果
     *
     * @param list
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> flatten(List<?> list) {
        List<T> result = new ArrayList<T>();
        for (Object o : list) {
            if (o instanceof List) {
                List<T> subResult = flatten((List<?>) o);
                result.addAll(subResult);
            } else {
                result.add((T) o);
            }
        }
        return result;
    }

    /**
     * 压缩集合，去掉集合中的null记录
     *
     * @param list
     * @param <T>
     * @return
     */
    public static <T> List<T> compact(List<T> list) {
        List<T> result = new ArrayList<T>();
        for (T t : list) {
            if (t != null) {
                result.add(t);
            }
        }
        return result;
    }

    /**
     * 提取集合中指定属性值
     *
     * @param list
     * @param property
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> List<R> map(List<T> list, String property) {
        List<R> result = new ArrayList<R>();
        for (T t : list) {

            try {
                R r = (R) getProperty(t, property);
                result.add(r);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return result;
    }

    /**
     * 将集合转换为map
     *
     * @param list
     * @param keyProperty
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> Map<K, V> toMap(List<V> list, String keyProperty) {
        Map<K, V> map = new HashMap<K, V>(100);
        for (V v : list) {

            try {
                K k = (K) getProperty(v, keyProperty);
                map.put(k, v);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return map;
    }

    /**
     * 将集合转换为map
     */
    public static <K, V, M> Map<K, V> toMap(List<M> list, String keyProperty, String valueProperty) {
        Map<K, V> map = new HashMap<>();
        for (M m : list) {

            try {
                K k = (K) getProperty(m, keyProperty);
                V v = (V) getProperty(m, valueProperty);
                map.put(k, v);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return map;
    }

    /**
     * 移除与value不想等的值，原集合不发生变化
     *
     * @param list
     * @param property
     * @param value
     * @param <T>
     * @return
     */
    public static <T> List<T> filter(List<T> list, String property, Object value) {
        List<T> result = new ArrayList<T>();
        for (T t : list) {
            try {
                Object v = getProperty(t, property);
                if ((v == null && value == null) || (v != null && v.equals(value))) {
                    result.add(t);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return result;
    }

    public interface Filter<T> {
        boolean execute(T t);
    }

    public static <T> List<T> filter(List<T> list, Filter<T> filter) {
        List<T> result = new ArrayList<T>();
        for (T t : list) {
            try {
                if (filter.execute(t)) {
                    result.add(t);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return result;
    }

    /**
     * 移除与value相等的值, 原数组不发生变化.
     *
     * @param list
     * @param value
     * @return
     */
    public static <T> List<T> without(List<T> list, T value) {
        List<T> result = new ArrayList<T>();
        for (T t : list) {
            if ((value == null && t == null)
                    || (value != null && value.equals(t))) {
                continue;
            }
            result.add(t);
        }
        return result;
    }

    /**
     * 对集合去重，原集合不发生变化
     *
     * @param input
     * @param <T>
     * @return
     */
    public static <T> List<T> uniq(List<T> input) {
        LinkedHashMap<T, T> map = new LinkedHashMap<>();
        for (T t : input) {
            map.put(t, t);
        }
        return new ArrayList<T>(map.values());
    }

    /**
     * 按照给定的集合（keys）进行排序
     *
     * @param input       要排序的集合
     * @param keyProperty 排序的属性
     * @param keys        给定的键值集合
     * @param <K>
     * @param <T>
     * @return
     */
    public static <K, T> List<T> sortBy(List<T> input, String keyProperty,
                                        List<K> keys) {
        if (input.isEmpty()) {
            return new ArrayList();
        }

        Map<K, T> map = toMap(input, keyProperty);
        List<T> result = new ArrayList<T>();
        for (K k : keys) {
            T t = map.get(k);
            if (t != null) {
                result.add(t);
            }
        }
        return result;
    }

    /**
     * 对集合进行分组
     *
     * @param input
     * @param keyProperty
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> Map<K, List<V>> group(List<V> input, String keyProperty) {
        Map<K, List<V>> result = new HashMap<>(100);

        for (V v : input) {

            try {
                K k = (K) getProperty(v, keyProperty);
                List<V> list = result.get(k);
                if (list == null) {
                    list = new ArrayList<>();
                    result.put(k, list);
                }
                list.add(v);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return result;
    }

    /**
     * 将数组按n个一份拆分.
     *
     * @param <T>
     * @param input
     * @param n
     * @return
     */
    public static <T> List<List<T>> group(List<T> input, int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must > 0");
        }

        int size = input.size();
        int m = (size + n - 1) / n;
        List<List<T>> result = new ArrayList<>(m);
        for (int i = 0; i < m; i++) {
            List<T> items = new ArrayList(n);
            int end = i < m - 1 ? n : size - i * n;
            for (int j = 0; j < end; j++) {
                items.add(input.get(i * n + j));
            }
            result.add(items);
        }
        return result;
    }


    public static boolean containAny(Set parent, Set child) {
        if (parent == null || child == null) {
            return false;
        }
        Iterator iter = child.iterator();
        while (iter.hasNext()) {
            return parent.contains(iter.next());
        }
        return false;
    }

    @SafeVarargs
    public static <V> List<V> newArrayList(V... vs) {
        List<V> list = new ArrayList<V>();
        for (V v : vs) {
            list.add(v);
        }
        return list;
    }

    private static <K, V> V addChildren(V vo, Map<K, V> map, String keyProperty, String parentProperty, K defaultParent, String childrenProperty) {
        map.forEach((id, item) -> {
            K k = (K) getProperty(vo, keyProperty);
            K pK = (K) getProperty(item, parentProperty);
            if (pK != null && pK.equals(k) && !pK.equals(defaultParent)) {
                List<V> children = (List<V>) getProperty(vo, childrenProperty);
                if (children == null) {
                    children = new ArrayList<>();
                }
                children.add(addChildren(item, map, keyProperty, parentProperty, defaultParent, childrenProperty));
                setProperty(vo, childrenProperty, children);
            }
        });
        return vo;
    }

    public static <K, V> List<V> getTree(List<V> list, String keyProperty, String parentProperty, K defaultParent, String childrenProperty) {
        Map<K, V> map = Lists.toMap(list, keyProperty);
        List<V> result = new ArrayList<>();
        list.forEach(vo -> {
            K k = (K) getProperty(vo, keyProperty);
            K pK = (K) getProperty(vo, parentProperty);
            if (!map.containsKey(pK) || (defaultParent != null && defaultParent.equals(k))) {
                result.add(vo);
            }
        });
        result.forEach(vo -> {
            addChildren(vo, map, keyProperty, parentProperty, defaultParent, childrenProperty);
        });
        return result;
    }

    private static Object getProperty(Object bean, String propertyName) {
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

    private static <V> void setProperty(Object bean, String propertyName, V value) {
        Method method = getMethod(bean, propertyName, "set", 1);
        if (method != null) {
            try {
                method.invoke(bean, value);
            } catch (Exception ignored) {
            }
        }
    }

    private static Field getField(Object bean, String propertyName) {
        for (Field f : bean.getClass().getDeclaredFields()) {
            if (propertyName.equals(f.getName())) {
                return f;
            }
        }
        return null;
    }

    private static Method getMethod(Object bean, String propertyName, String pre, int paramLength) {
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
