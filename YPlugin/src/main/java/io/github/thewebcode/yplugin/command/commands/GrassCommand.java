package io.github.thewebcode.yplugin.command.commands;

import io.github.thewebcode.yplugin.block.Blocks;
import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.command.Arg;
import io.github.thewebcode.yplugin.command.Command;
import io.github.thewebcode.yplugin.permission.Perms;
import io.github.thewebcode.yplugin.player.Players;
import org.bukkit.entity.Player;

public class GrassCommand {
    @Command(identifier = "grass", permissions = Perms.COMMAND_GRASS)
    public void onGrassCommand(Player player, @Arg(name = "size") int size, @Arg(name = "density", def = "37") int density) {
        Blocks.regrowGrass(Players.getTargetLocation(player), size, density);
        Chat.actionMessage(player, "&aGrass hath been grown!");
    }
}
