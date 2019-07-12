package com.github.middleware.httpadapter.core;

import java.util.List;
import java.util.Optional;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/1/9.
 */
public interface ExtensionLoader<T> {
    List<T> getExtensions();

    Optional<T> getExtension();

    Optional<T> getExtension(String regex);

    Optional<T> getExtension(Class impClz);
}
