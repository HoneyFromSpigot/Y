package io.github.thewebcode.yplugin.command.commands;

import io.github.thewebcode.yplugin.YPlugin;
import io.github.thewebcode.yplugin.Messages;
import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.command.Arg;
import io.github.thewebcode.yplugin.command.Command;
import io.github.thewebcode.yplugin.permission.Perms;
import io.github.thewebcode.yplugin.player.MinecraftPlayer;
import io.github.thewebcode.yplugin.player.Players;
import org.bukkit.entity.Player;

public class SpeedCommand {
    private Players players;

    public SpeedCommand() {
        players = YPlugin.getInstance().getPlayerHandler();
    }

    @Command(identifier = "speed", permissions = {Perms.SPEED_COMMAND})
    public void onSpeedCommand(Player player, @Arg(name = "speed", def = "1", verifiers = "min[1.0]|max[7.8]") double speed) {
        MinecraftPlayer minecraftPlayer = players.getData(player);
        if (player.isFlying()) {
            double fSpeed = 0;

            if (speed == 1) {
                fSpeed = MinecraftPlayer.DEFAULT_FLY_SPEED;
            } else {
                fSpeed = (speed + (MinecraftPlayer.DEFAULT_FLY_SPEED * 10)) / 10;
            }

            minecraftPlayer.setFlySpeed(fSpeed);
        } else {
            double wSpeed = 0;
            if (speed == 1) {
                wSpeed = MinecraftPlayer.DEFAULT_WALK_SPEED;
            } else {
                wSpeed = (speed + (MinecraftPlayer.DEFAULT_WALK_SPEED * 10)) / 10;
            }
            minecraftPlayer.setWalkSpeed(wSpeed);
        }

        Chat.message(player, Messages.playerSpeedUpdated(player.isFlying(), (int) speed));

    }
}
