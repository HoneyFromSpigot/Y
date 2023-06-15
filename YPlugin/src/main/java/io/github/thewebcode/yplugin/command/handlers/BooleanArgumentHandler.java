package io.github.thewebcode.yplugin.command.handlers;

import io.github.thewebcode.yplugin.command.ArgumentHandler;
import io.github.thewebcode.yplugin.command.CommandArgument;
import io.github.thewebcode.yplugin.command.TransformError;
import org.bukkit.command.CommandSender;

public class BooleanArgumentHandler extends ArgumentHandler<Boolean> {

    public BooleanArgumentHandler() {
        setMessage("parse_error", "The parameter [%p] is not a boolean");
    }

    @Override
    public Boolean transform(CommandSender sender, CommandArgument argument, String value) throws TransformError {
        switch (value.toLowerCase()) {
            case "yes":
            case "true":
            case "y":
                return true;
            case "no":
            case "false":
            case "n":
                return false;
            default:
                throw new TransformError(getMessage("parse_error"));
        }
    }
}
