package io.github.thewebcode.yplugin.command.commands;

import com.mysql.cj.util.StringUtils;
import io.github.thewebcode.yplugin.Messages;
import io.github.thewebcode.yplugin.YPlugin;
import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.command.Arg;
import io.github.thewebcode.yplugin.command.Command;
import io.github.thewebcode.yplugin.command.Wildcard;
import io.github.thewebcode.yplugin.item.ItemSetManager;
import io.github.thewebcode.yplugin.permission.Perms;
import io.github.thewebcode.yplugin.player.Players;
import org.bukkit.entity.Player;

public class SetCommand {
    private static ItemSetManager sets;

    public SetCommand() {
        sets = YPlugin.getInstance().getItemSetManager();
    }

    @Command(identifier = "set", permissions = Perms.SET_COMMAND)
    public void onSetCommand(Player player, @Wildcard @Arg(name = "name") String name) {
        if (StringUtils.isNullOrEmpty(name)) {
            Chat.message(player, Messages.invalidCommandUsage("name(can include spaces)"));
            return;
        }

        ItemSetManager.ItemSet set = sets.getSet(name);

        if (set == null) {
            Chat.message(player, String.format("&cThe set '&e%s'&c doesn't exist", name));
            return;
        }

        Players.setInventory(player, set.getInventoryContents(), true);
        Chat.message(player, String.format("&eYour inventory has been set to the '&a%s&e' item set", set.getName()));
    }

    @Command(identifier = "set list")
    public void onSetListCommand(Player player) {
        for (String name : sets.getSetNames()) {
            Chat.message(player, name);
        }
    }

    @Command(identifier = "set save")
    public void onSetSaveCommand(Player player, @Wildcard @Arg(name = "name") String name) {
        if (StringUtils.isNullOrEmpty(name)) {
            Chat.message(player, Messages.invalidCommandUsage("name(can include spaces)"));
            return;
        }

        ItemSetManager.ItemSet set = new ItemSetManager.ItemSet(name, player.getInventory());
        sets.addSet(set);

        Chat.message(player, String.format("&aSaved the set &e%s&a to file!", name));

    }

}
