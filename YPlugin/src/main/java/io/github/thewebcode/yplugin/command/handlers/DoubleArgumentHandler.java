package io.github.thewebcode.yplugin.command.handlers;

import io.github.thewebcode.yplugin.command.CommandArgument;
import io.github.thewebcode.yplugin.command.TransformError;
import org.bukkit.command.CommandSender;

public class DoubleArgumentHandler extends NumberArgumentHandler<Double> {
    public DoubleArgumentHandler() {
        setMessage("parse_error", "The parameter [%p] is not a number");
    }

    @Override
    public Double transform(CommandSender sender, CommandArgument argument, String value) throws TransformError {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            throw new TransformError(argument.getMessage("parse_error"));
        }
    }
}
