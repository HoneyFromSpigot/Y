package io.github.thewebcode.yplugin.yml.converter;

import io.github.thewebcode.yplugin.yml.InternalConverter;

import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArrayYamlConverter implements Converter {
    private InternalConverter internalConverter;

    public ArrayYamlConverter(InternalConverter internalConverter) {
        this.internalConverter = internalConverter;
    }

    @Override
    public Object toConfig(Class<?> type, Object obj, ParameterizedType parameterizedType) throws Exception {
        Class<?> singleType = type.getComponentType();
        Converter conv = internalConverter.getConverter(singleType);
        if (conv == null) {
            return obj;
        }

        Object[] ret = new Object[Array.getLength(obj)];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = conv.toConfig(singleType, Array.get(obj, i), parameterizedType);
        }
        return ret;
    }

    @Override
    public Object fromConfig(Class type, Object section, ParameterizedType genericType) throws Exception {
        Class<?> singleType = type.getComponentType();
        List values;

        if (section instanceof List) {
            values = (List) section;
        } else {
            values = new ArrayList();
            Collections.addAll(values, (Object[]) section);
        }

        Object ret = Array.newInstance(singleType, values.size());
        Converter conv = internalConverter.getConverter(singleType);
        if (conv == null) {
            return values.toArray((Object[]) ret);
        }

        for (int i = 0; i < values.size(); i++) {
            Array.set(ret, i, conv.fromConfig(singleType, values.get(i), genericType));
        }
        return ret;
    }

    @Override
    public boolean supports(Class<?> type) {
        return type.isArray();
    }
}
