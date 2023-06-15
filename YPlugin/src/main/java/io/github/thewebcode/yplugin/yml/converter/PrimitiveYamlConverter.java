package io.github.thewebcode.yplugin.yml.converter;

import io.github.thewebcode.yplugin.yml.InternalConverter;

import java.lang.reflect.ParameterizedType;
import java.util.HashSet;

public class PrimitiveYamlConverter implements Converter {
    private HashSet<String> types = new HashSet<String>() {{
        add("boolean");
        add("char");
        add("byte");
        add("short");
        add("int");
        add("long");
        add("float");
        add("double");
    }};

    private InternalConverter internalConverter;

    public PrimitiveYamlConverter(InternalConverter internalConverter) {
        this.internalConverter = internalConverter;
    }

    @Override
    public Object toConfig(Class<?> type, Object obj, ParameterizedType parameterizedType) throws Exception {
        return obj;
    }

    @Override
    public Object fromConfig(Class type, Object section, ParameterizedType genericType) throws Exception {
        switch (type.getSimpleName()) {
            case "short":
                return (section instanceof Short) ? section : (short) ((int) section);
            case "byte":
                return (section instanceof Byte) ? section : (byte) ((int) section);
            case "float":
                if (section instanceof Integer) {
                    return (float) (double) ((int) section);
                }

                return (section instanceof Float) ? section : (float) ((double) section);
            case "char":
                return (section instanceof Character) ? section : ((String) section).charAt(0);
            default:
                return section;
        }
    }

    @Override
    public boolean supports(Class<?> type) {
        return types.contains(type.getName());
    }
}
