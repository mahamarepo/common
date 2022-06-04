package com.mahama.common.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class JarLoader extends URLClassLoader {
    private static JarLoader loader = null;

    private JarLoader() {
        super(new URL[0], JarLoader.class.getClassLoader());
    }

    public JarLoader(ClassLoader classLoader) {
        super(new URL[0], classLoader);
    }

    public static JarLoader getInstance() {
        if (loader == null)
            loader = new JarLoader();
        return loader;
    }

    public static JarLoader getInstance(String url) throws MalformedURLException {
        if (loader == null)
            loader = new JarLoader();
        loader.addURL(url);
        return loader;
    }

    public Class<?> getClazz(String className) throws ClassNotFoundException {
        return Class.forName(className, true, loader);
    }

    public <T> T getInitializeClazz(String className) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return (T)Class.forName(className, true, loader).newInstance();
    }

    public void addURL(String url) throws MalformedURLException {
        this.addURL(new URL(url));
    }
}

