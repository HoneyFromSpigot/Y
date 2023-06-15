package io.github.thewebcode.yplugin.command.commands;

import io.github.thewebcode.yplugin.command.Command;
import io.github.thewebcode.yplugin.inventory.Inventories;
import io.github.thewebcode.yplugin.permission.Perms;
import org.bukkit.entity.Player;

public class WorkbenchCommand {
    @Command(identifier = "workbench", permissions = Perms.COMMAND_WORKBENCH)
    public void onWorkbenchCommand(Player player) {
        Inventories.openWorkbench(player);
    }
}
