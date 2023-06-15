package io.github.thewebcode.yplugin.command;

import org.bukkit.command.CommandSender;


public interface ArgumentVariable<T> {
    public T var(CommandSender sender, CommandArgument argument, String varName) throws CommandError;
}
