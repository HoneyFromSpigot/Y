package io.github.thewebcode.yplugin.command;

import io.github.thewebcode.yplugin.gui.ScrollingGui;
import io.github.thewebcode.yplugin.gui.builder.gui.ScrollingBuilder;
import io.github.thewebcode.yplugin.gui.builder.gui.SimpleBuilder;
import io.github.thewebcode.yplugin.gui.builder.item.ItemBuilder;
import io.github.thewebcode.yplugin.gui.components.GuiType;
import io.github.thewebcode.yplugin.gui.components.ScrollType;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TestCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return false;
    }
}
