package io.github.thewebcode.yplugin.command.commands;

import io.github.thewebcode.yplugin.Messages;
import io.github.thewebcode.yplugin.YPlugin;
import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.command.Command;
import io.github.thewebcode.yplugin.location.PreTeleportLocation;
import io.github.thewebcode.yplugin.permission.Perms;
import io.github.thewebcode.yplugin.player.MinecraftPlayer;
import io.github.thewebcode.yplugin.player.Players;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class BackCommand {

    private Players players = YPlugin.getInstance().getPlayerHandler();

    public BackCommand() {

    }

    @Command(identifier = "back", onlyPlayers = true, permissions = {Perms.COMMAND_BACK})
    public void onBackCommand(Player player) {
        MinecraftPlayer minecraftPlayer = players.getData(player);
        Location location = minecraftPlayer.getPreTeleportLocation();

        if (location == null) {
            Chat.message(player, Messages.NO_TELEPORT_BACK_LOCATION);
            return;
        }

        PreTeleportLocation preTeleLoc = minecraftPlayer.getPreTeleportLocation();

        Players.teleport(player, preTeleLoc);
    }
}
