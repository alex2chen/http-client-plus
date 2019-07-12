package com.github.middleware.httpadapter.core.util;

import com.github.middleware.httpadapter.core.ExtensionLoader;
import com.google.common.base.Strings;
import org.springframework.util.ClassUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/1/9.
 */
public class DefaultExtensionLoader<T> implements ExtensionLoader<T> {
    private final ServiceLoader<T> serviceLoader;
    private List<T> extTypes;

    public DefaultExtensionLoader(Class<T> clz) {
        this(clz, Thread.currentThread().getContextClassLoader());
    }

    public DefaultExtensionLoader(Class<T> clz, ClassLoader classLoader) {
        extTypes = new ArrayList<>();
        serviceLoader = ServiceLoader.load(clz, classLoader);
        for (T service : serviceLoader) {
            extTypes.add(service);
        }
    }

    @Override
    public List<T> getExtensions() {
        return extTypes;
    }

    @Override
    public Optional<T> getExtension() {
        if (isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(getExtensions().get(0));
    }

    private boolean isEmpty() {
        List<T> list = getExtensions();
        return list == null || list.isEmpty();
    }

    @Override
    public Optional<T> getExtension(String regex) {
        if (isEmpty()) {
            return Optional.empty();
        }
        if (!Strings.isNullOrEmpty(regex)) {
            AtomicReference<T> result = new AtomicReference<>();
            List<T> list = getExtensions();
            list.stream().filter(x -> x.getClass().getName().contains(regex)).findFirst().ifPresent(result::set);
            return Optional.ofNullable(result.get());
        }
        return Optional.ofNullable(getExtensions().get(0));
    }

    @Override
    public Optional<T> getExtension(final Class impClz) {
        if (isEmpty()) {
            return Optional.empty();
        }
        List<T> list = getExtensions();
        return list.stream().filter(x -> ClassUtils.isAssignable(x.getClass(), impClz)).findFirst();
    }
}

