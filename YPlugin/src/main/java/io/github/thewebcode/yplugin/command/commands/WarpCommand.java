package io.github.thewebcode.yplugin.command.commands;

import io.github.thewebcode.yplugin.Messages;
import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.command.Arg;
import io.github.thewebcode.yplugin.command.Command;
import io.github.thewebcode.yplugin.command.Wildcard;
import io.github.thewebcode.yplugin.permission.Perms;
import io.github.thewebcode.yplugin.player.Players;
import io.github.thewebcode.yplugin.warp.Warp;
import io.github.thewebcode.yplugin.warp.Warps;
import org.bukkit.entity.Player;

public class WarpCommand {
    @Command(identifier = "warp", permissions = Perms.COMMAND_WARP)
    public void onWarpCommand(Player player, @Wildcard @Arg(name = "warp") String warpName) {
        if (!Warps.isWarp(warpName)) {
            Chat.message(player, Messages.invalidWarp(warpName));
            return;
        }

        Warp warp = Warps.getWarp(warpName);
        Players.teleport(player, warp);
        Chat.message(player, Messages.playerWarpedTo(warpName));
    }
}
