package io.github.thewebcode.yplugin.command.handlers;

import io.github.thewebcode.yplugin.command.ArgumentHandler;
import io.github.thewebcode.yplugin.command.CommandArgument;
import io.github.thewebcode.yplugin.command.TransformError;
import io.github.thewebcode.yplugin.exceptions.InvalidMaterialNameException;
import io.github.thewebcode.yplugin.item.Items;
import org.bukkit.command.CommandSender;
import org.bukkit.material.MaterialData;

public class MaterialDataArgumentHandler extends ArgumentHandler<MaterialData> {

    public MaterialDataArgumentHandler() {
    }

    @Override
    public MaterialData transform(CommandSender sender, CommandArgument argument, String value) throws TransformError {
        MaterialData data = null;

        try {
            data = Items.getMaterialDataFromString(value);
        } catch (InvalidMaterialNameException e) {
            throw new TransformError(e.getMessage());
        }

        return data;
    }
}
