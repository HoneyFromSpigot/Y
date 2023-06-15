package io.github.thewebcode.yplugin.debug.actions;

import io.github.thewebcode.yplugin.YPlugin;
import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.debug.DebugAction;
import io.github.thewebcode.yplugin.event.StackTraceEvent;
import io.github.thewebcode.yplugin.player.Players;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class DebugThrowException implements DebugAction {
    @Override
    public void doAction(Player player, String... args) {
        YPlugin.getInstance().registerListeners(new Listener() {
            @EventHandler
            public void onPlayerDropItem(PlayerDropItemEvent e) {
                if (Players.isDebugging(e.getPlayer())) {
                    throw new RuntimeException("DEBUGGNG AN EXCEPTION THROW FOR STACK-TRACE-EVENT-PURPOSES");
                }
            }

            @EventHandler
            public void onStackTraceEvent(StackTraceEvent e) {
                Chat.messageAll(Players.getAllDebugging(), "Exception Called: " + e.getException().toString());
            }
        });
        Chat.message(player, "&cRegistered Error-Ridden DropItem Listener");
    }

    @Override
    public String getActionName() {
        return "throw_exception";
    }
}
