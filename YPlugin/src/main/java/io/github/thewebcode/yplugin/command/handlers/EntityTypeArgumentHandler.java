package io.github.thewebcode.yplugin.command.handlers;

import io.github.thewebcode.yplugin.command.ArgumentHandler;
import io.github.thewebcode.yplugin.command.CommandArgument;
import io.github.thewebcode.yplugin.command.TransformError;
import io.github.thewebcode.yplugin.entity.Entities;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;


public class EntityTypeArgumentHandler extends ArgumentHandler<EntityType> {
    public EntityTypeArgumentHandler() {
        setMessage("parse_error", "There is no entity named %1");
        setMessage("include_error", "There is no entity named %1");
        setMessage("exclude_error", "There is no entity named %1");
    }

    @Override
    public EntityType transform(CommandSender sender, CommandArgument argument, String value) throws TransformError {
        EntityType type = null;

        if (StringUtils.isNumeric(value)) {
            type = EntityType.fromId(Integer.parseInt(value));
        } else {
            type = Entities.getTypeByName(value);
        }

        if (type == null) {
            throw new TransformError(argument.getMessage("parse_error", value));
        }

        return type;

    }


}
