package com.mahama.common.utils;

import java.util.*;

/**
 * 集合工具类
 */
public final class Lists {

    private Lists() {
    }


    /**
     * 碾平集合咯，主要针对集合元素为集合的情况有效果
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
     */
    public static <T, R> List<R> map(List<T> list, String property) {
        List<R> result = new ArrayList<R>();
        for (T t : list) {

            try {
                R r = (R) FieldUtil.getProperty(t, property);
                result.add(r);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return result;
    }

    /**
     * 将集合转换为map
     */
    public static <K, V> Map<K, V> toMap(List<V> list, String keyProperty) {
        Map<K, V> map = new HashMap<K, V>(100);
        for (V v : list) {

            try {
                K k = (K) FieldUtil.getProperty(v, keyProperty);
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
                K k = (K) FieldUtil.getProperty(m, keyProperty);
                V v = (V) FieldUtil.getProperty(m, valueProperty);
                map.put(k, v);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return map;
    }

    /**
     * 移除与value不想等的值，原集合不发生变化
     */
    public static <T> List<T> filter(List<T> list, String property, Object value) {
        List<T> result = new ArrayList<T>();
        for (T t : list) {
            try {
                Object v = FieldUtil.getProperty(t, property);
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
     */
    public static <K, T> List<T> sortBy(List<T> input, String keyProperty,
                                        List<K> keys) {
        if (input.isEmpty()) {
            return new ArrayList<T>();
        }

        Map<K, T> map = toMap(input, keyProperty);
        List<T> result = new ArrayList<T>();
        if (keys != null && keys.size() > 0) {
            for (K k : keys) {
                T t = map.get(k);
                if (t != null) {
                    result.add(t);
                }
            }
        } else {
            map.forEach((k, t) -> result.add(t));
        }
        return result;
    }

    /**
     * 对集合进行分组
     */
    public static <K, V> Map<K, List<V>> group(List<V> input, String keyProperty) {
        Map<K, List<V>> result = new HashMap<>(100);

        for (V v : input) {
            try {
                K k = (K) FieldUtil.getProperty(v, keyProperty);
                if (k == null) {
                    continue;
                }
                List<V> list = result.computeIfAbsent(k, k1 -> new ArrayList<>());
                list.add(v);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return result;
    }

    /**
     * 将数组按n个一份拆分.
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

    /**
     * 对集合进行分组
     */
    public static <K, V> Map<K, Integer> groupToCount(List<V> input, String keyProperty) {
        Map<K, List<V>> groupMap = group(input, keyProperty);
        Map<K, Integer> result = new HashMap<>(100);
        groupMap.forEach((k, items) -> {
            result.put(k, items.size());
        });
        return result;
    }


    public static boolean containAny(Set parent, Set child) {
        if (parent == null || child == null) {
            return false;
        }
        for (Object o : child) {
            return parent.contains(o);
        }
        return false;
    }

    @SafeVarargs
    public static <V> List<V> newArrayList(V... vs) {
        List<V> list = new ArrayList<V>();
        Collections.addAll(list, vs);
        return list;
    }

    private static <K, V> V addChildren(V vo, Map<K, V> map, String keyProperty, String parentProperty, K defaultParent, String childrenProperty) {
        map.forEach((id, item) -> {
            K k = (K) FieldUtil.getProperty(vo, keyProperty);
            K pK = (K) FieldUtil.getProperty(item, parentProperty);
            if (pK != null && pK.equals(k) && !pK.equals(defaultParent)) {
                List<V> children = (List<V>) FieldUtil.getProperty(vo, childrenProperty);
                if (children == null) {
                    children = new ArrayList<>();
                }
                children.add(addChildren(item, map, keyProperty, parentProperty, defaultParent, childrenProperty));
                FieldUtil.setProperty(vo, childrenProperty, children);
            }
        });
        return vo;
    }

    public static <K, V> List<V> getTree(List<V> list, String keyProperty, String parentProperty, K defaultParent, String childrenProperty) {
        Map<K, V> map = Lists.toMap(list, keyProperty);
        List<V> result = new ArrayList<>();
        list.forEach(vo -> {
            K k = (K) FieldUtil.getProperty(vo, keyProperty);
            K pK = (K) FieldUtil.getProperty(vo, parentProperty);
            if (!map.containsKey(pK) || (defaultParent != null && defaultParent.equals(k))) {
                result.add(vo);
            }
        });
        result.forEach(vo -> {
            addChildren(vo, map, keyProperty, parentProperty, defaultParent, childrenProperty);
        });
        return result;
    }
}
