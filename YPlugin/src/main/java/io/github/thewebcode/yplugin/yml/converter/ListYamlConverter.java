package io.github.thewebcode.yplugin.yml.converter;

import io.github.thewebcode.yplugin.yml.InternalConverter;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

public class ListYamlConverter implements Converter {
    private InternalConverter internalConverter;

    public ListYamlConverter(InternalConverter internalConverter) {
        this.internalConverter = internalConverter;
    }

    @Override
    public Object toConfig(Class<?> type, Object obj, ParameterizedType genericType) throws Exception {
        List values = (List) obj;
        List newList = new ArrayList();

        for (Object val : values) {
            Converter converter = internalConverter.getConverter(val.getClass());

            if (converter != null) {
                newList.add(converter.toConfig(val.getClass(), val, null));
            } else {
                newList.add(val);
            }
        }

        return newList;
    }

    @Override
    public Object fromConfig(Class type, Object section, ParameterizedType genericType) throws Exception {
        List newList = new ArrayList();
        try {
            newList = ((List) type.newInstance());
        } catch (Exception e) {
        }

        List values = (List) section;

        if (genericType != null && genericType.getActualTypeArguments()[0] instanceof Class) {
            Converter converter = internalConverter.getConverter((Class) genericType.getActualTypeArguments()[0]);

            if (converter != null) {
                for (int i = 0; i < values.size(); i++) {
                    newList.add(converter.fromConfig((Class) genericType.getActualTypeArguments()[0], values.get(i), null));
                }
            } else {
                newList = values;
            }
        } else {
            newList = values;
        }

        return newList;
    }

    @Override
    public boolean supports(Class<?> type) {
        return List.class.isAssignableFrom(type);
    }
}
