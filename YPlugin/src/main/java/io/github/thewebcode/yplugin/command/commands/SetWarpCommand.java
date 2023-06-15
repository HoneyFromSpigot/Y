package io.github.thewebcode.yplugin.command.commands;

import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.command.Arg;
import io.github.thewebcode.yplugin.command.Command;
import io.github.thewebcode.yplugin.command.Wildcard;
import io.github.thewebcode.yplugin.permission.Perms;
import io.github.thewebcode.yplugin.warp.Warp;
import io.github.thewebcode.yplugin.warp.Warps;
import org.bukkit.entity.Player;

import static io.github.thewebcode.yplugin.Messages.duplicateWarp;
import static io.github.thewebcode.yplugin.Messages.warpCreated;

public class SetWarpCommand {
    @Command(identifier = "setwarp", permissions = Perms.COMMAND_SETWARP)
    public void onSetWarpCommand(Player player, @Wildcard @Arg(name = "warp name") String warpName) {
        if (Warps.isWarp(warpName)) {
            Chat.message(player, duplicateWarp(warpName));
            return;
        }

        Warps.addWarp(new Warp(warpName, player.getLocation()), true);
        Chat.message(player, warpCreated(warpName));
    }
}
