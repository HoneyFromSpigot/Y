package io.github.thewebcode.yplugin.yml.converter;

import java.lang.reflect.ParameterizedType;

public interface Converter {
    public Object toConfig(Class<?> type, Object obj, ParameterizedType parameterizedType) throws Exception;

    public Object fromConfig(Class<?> type, Object obj, ParameterizedType parameterizedType) throws Exception;

    public boolean supports(Class<?> type);
}
