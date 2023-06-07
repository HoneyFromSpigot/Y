package io.github.thewebcode.yplugin.event;

import io.github.thewebcode.yplugin.networking.MessageOutboundHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Eventlistener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        MessageOutboundHandler handler = new MessageOutboundHandler.Builder(player).build();
    }
}
