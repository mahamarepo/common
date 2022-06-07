package com.mahama.common.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CfgUtil {
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    public static String getDefaultConfName() {
        return getDefaultConfName(0L);
    }

    public static String getDefaultConfName(long id) {
        if (id > 0) {
            return "conf_" + id;
        } else {
            return "conf";
        }
    }

    public static String getConfPath(String name) {
        return "config/" + name + ".properties";
    }

    public static Integer getInt(String key) {
        return getInt(key, null);
    }

    public static Integer getInt(String key, Integer defaultValue) {
        return getInt(getDefaultConfName(), key, defaultValue);
    }

    public static Integer getInt(String confName, String key, Integer defaultValue) {
        return getInt(confName, key, defaultValue, null);
    }

    public static Integer getInt(String confName, String key, Integer defaultValue, String comments) {
        return getValue(confName, key, defaultValue, comments, Integer::valueOf);
    }

    public static Boolean getBoolean(String key) {
        return getBoolean(key, null);
    }

    public static Boolean getBoolean(String key, Boolean defaultValue) {
        return getBoolean(getDefaultConfName(), key, defaultValue);
    }

    public static Boolean getBoolean(String confName, String key, Boolean defaultValue) {
        return getBoolean(confName, key, defaultValue, null);
    }

    public static Boolean getBoolean(String confName, String key, Boolean defaultValue, String comments) {
        return getValue(confName, key, defaultValue, comments, Boolean::valueOf);
    }

    public static String getString(String key) {
        return getString(key, null);
    }

    public static String getString(String key, String defaultValue) {
        return getString(getDefaultConfName(), key, defaultValue);
    }


    public static String getString(String confName, String key, String defaultValue) {
        return getString(confName, key, defaultValue, null);
    }

    public static String getString(String confName, String key, String defaultValue, String comments) {
        return getValue(confName, key, defaultValue, comments, v -> v);
    }

    interface ValueOfTask<T> {
        T execute(String value);
    }


    private static <V> V getValue(String confName, String key, V defaultValue, String comments, ValueOfTask<V> task) {
        String value = getProperty(key);
        if (value == null && defaultValue != null) {
            setProperty(confName, key, String.valueOf(defaultValue), comments);
            return defaultValue;
        } else if (value != null) {
            return task.execute(value);
        }
        return null;
    }

    public static <V> void setProperty(String key, V value) {
        setProperty(getDefaultConfName(), key, value, null);
    }

    public static <V> void setProperty(String confName, String key, V value) {
        setProperty(confName, key, value, null);
    }

    public static <V> void setProperty(String confName, String key, V value, String comments) {
        File conf = new File(getConfPath(confName));
        try {
            if (!conf.exists()) {
                if (conf.createNewFile()) {
                    executor.execute(() -> saveProperties(confName, key, value, comments));
                }
            } else {
                executor.execute(() -> saveProperties(confName, key, value, comments));
            }
        } catch (Exception ignored) {
        }
    }

    public static String getProperty(String key) {
        return getProperty(getDefaultConfName(), key);
    }

    public static String getProperty(String confName, String key) {
        return getProperty(confName, key, null);
    }

    public static String getProperty(String confName, String key, String defaultValue) {
        FileInputStream in = null;
        BufferedReader bf = null;
        String value = defaultValue;
        try {
            Properties properties = new Properties();
            in = new FileInputStream(getConfPath(confName));
            bf = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            properties.load(bf);
            value = properties.getProperty(key);
            if (value == null) {
                value = defaultValue;
            }
        } catch (IOException ignored) {
        } finally {
            try {
                if (bf != null)
                    bf.close();
                if (in != null)
                    in.close();
            } catch (IOException ignored) {
            }
        }
        return value;
    }

    private static <V> void saveProperties(String confName, String key, V value, String comments) {
        FileOutputStream oFile = null;
        FileInputStream in = null;
        BufferedReader bf = null;
        String path = getConfPath(confName);
        try {
            Properties properties = new Properties();
            in = new FileInputStream(path);
            bf = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            properties.load(bf);
            properties.setProperty(key, String.valueOf(value));
            if (properties.containsKey(key)) {
                oFile = new FileOutputStream(path);
            } else {
                oFile = new FileOutputStream(path, true);
            }
            properties.store(new OutputStreamWriter(oFile, StandardCharsets.UTF_8), comments);
        } catch (IOException ignored) {
        } finally {
            try {
                if (bf != null)
                    bf.close();
                if (in != null)
                    in.close();
                if (oFile != null)
                    oFile.close();
            } catch (IOException ignored) {
            }
        }
    }
}
