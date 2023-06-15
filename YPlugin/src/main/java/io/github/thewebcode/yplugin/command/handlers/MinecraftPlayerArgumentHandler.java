package io.github.thewebcode.yplugin.command.handlers;

import io.github.thewebcode.yplugin.Messages;
import io.github.thewebcode.yplugin.YPlugin;
import io.github.thewebcode.yplugin.command.ArgumentHandler;
import io.github.thewebcode.yplugin.command.CommandArgument;
import io.github.thewebcode.yplugin.command.CommandError;
import io.github.thewebcode.yplugin.command.TransformError;
import io.github.thewebcode.yplugin.player.MinecraftPlayer;
import io.github.thewebcode.yplugin.player.Players;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MinecraftPlayerArgumentHandler extends ArgumentHandler<MinecraftPlayer> {

    private static YPlugin commons = YPlugin.getInstance();

    public MinecraftPlayerArgumentHandler() {
        setMessage("player_not_online", "%1 isn't online!");

        addVariable("sender", "The command executor", (sender, argument, varName) -> {
            if (!(sender instanceof Player)) {
                throw new CommandError(Messages.CANT_AS_CONSOLE);
            }

            Player player = (Player) sender;
            MinecraftPlayer mcPlayer = commons.getPlayerHandler().getData(player);
            if (mcPlayer == null) {
                throw new CommandError("No MinecraftPlayer data exists for " + player.getName());
            }
            return mcPlayer;
        });
    }

    @Override
    public MinecraftPlayer transform(CommandSender sender, CommandArgument argument, String value) throws TransformError {
        Player player = Players.getPlayer(value);

        if (player == null) {
            throw new TransformError(Messages.playerOffline(value));
        }

        return commons.getPlayerHandler().getData(player);
    }
}
