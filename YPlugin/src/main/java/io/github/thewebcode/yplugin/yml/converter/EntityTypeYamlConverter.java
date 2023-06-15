package io.github.thewebcode.yplugin.yml.converter;

import io.github.thewebcode.yplugin.entity.MobType;
import io.github.thewebcode.yplugin.yml.ConfigSection;
import io.github.thewebcode.yplugin.yml.InternalConverter;
import org.bukkit.entity.EntityType;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

public class EntityTypeYamlConverter implements Converter {
    private InternalConverter converter;

    public EntityTypeYamlConverter(InternalConverter converter) {
        this.converter = converter;
    }

    @Override
    public Object toConfig(Class<?> type, Object obj, ParameterizedType parameterizedType) throws Exception {
        org.bukkit.entity.EntityType entityType = (org.bukkit.entity.EntityType) obj;
        Map<String, Object> saveMap = new HashMap<>();
        saveMap.put("entity-type", entityType.name().toLowerCase());
        return saveMap;
    }

    @Override
    public Object fromConfig(Class<?> type, Object section, ParameterizedType parameterizedType) throws Exception {
        Map<String, Object> entityData;

        if (section instanceof Map) {
            entityData = (Map<String, Object>) section;
        } else {
            entityData = (Map<String, Object>) ((ConfigSection) section).getRawMap();
        }

        EntityType entityType = MobType.getTypeByName((String) entityData.get("entity-type"));

        return entityType;
    }

    @Override
    public boolean supports(Class<?> type) {
        return type.isAssignableFrom(org.bukkit.entity.EntityType.class);
    }
}
