package io.github.thewebcode.yplugin.debug.actions;


import io.github.thewebcode.yplugin.YPlugin;
import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.debug.DebugAction;
import io.github.thewebcode.yplugin.player.MinecraftPlayer;
import org.bukkit.entity.Player;

public class DebugForceRespawn implements DebugAction {
    @Override
    public void doAction(Player player, String... args) {
        MinecraftPlayer mcPlayer = YPlugin.getInstance().getPlayerHandler().getData(player);

        if (mcPlayer.hasForceRespawn()) {
            mcPlayer.setForceRespawn(false);
            Chat.message(player, "&eForce Respawn has been &cdisabled");
        } else {
            mcPlayer.setForceRespawn(true);
            Chat.message(player,"&eForce Respawn has been &aenabled");
        }
    }

    @Override
    public String getActionName() {
        return "force_respawn";
    }
}
