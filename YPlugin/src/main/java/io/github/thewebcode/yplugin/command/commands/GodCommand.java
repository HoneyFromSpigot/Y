package io.github.thewebcode.yplugin.command.commands;

import io.github.thewebcode.yplugin.YPlugin;
import io.github.thewebcode.yplugin.Messages;
import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.command.Command;
import io.github.thewebcode.yplugin.permission.Perms;
import io.github.thewebcode.yplugin.player.MinecraftPlayer;
import org.bukkit.entity.Player;

public class GodCommand {
    @Command(identifier = "god", permissions = Perms.COMMAND_GOD_MODE, description = "Become the almighty!!")
    public void onGodCommand(Player player) {
        MinecraftPlayer user = YPlugin.getInstance().getPlayerHandler().getData(player);
        boolean god = user.hasGodMode();
        user.setGodMode(!god);

        if (user.hasGodMode()) {
            Chat.message(player, Messages.GOD_MODE_ENABLED);
        } else {
            Chat.message(player, Messages.GOD_MODE_DISABLED);
        }
    }
}
