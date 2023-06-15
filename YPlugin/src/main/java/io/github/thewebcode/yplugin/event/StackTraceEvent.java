package io.github.thewebcode.yplugin.event;

import io.github.thewebcode.yplugin.Messages;
import io.github.thewebcode.yplugin.YPlugin;
import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.config.Configuration;
import io.github.thewebcode.yplugin.debug.Debugger;
import io.github.thewebcode.yplugin.nms.NMS;
import io.github.thewebcode.yplugin.nms.UnhandledStackTrace;
import io.github.thewebcode.yplugin.player.Players;
import io.github.thewebcode.yplugin.plugin.Plugins;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public class StackTraceEvent extends Event {
    private static UnhandledStackTrace errorHandler = NMS.getStackTraceHandler();

    private static final HandlerList handler = new HandlerList();
    private Throwable throwable;

    public StackTraceEvent(Throwable ex) {
        this.throwable = ex;
    }

    @Override
    public HandlerList getHandlers() {
        return handler;
    }

    public static HandlerList getHandlerList() {
        return handler;
    }

    public Throwable getException() {
        return throwable;
    }

    public static void call(Throwable throwable) {
        Chat.messageConsole("Calling Stack Trace Event!");
        StackTraceEvent event = new StackTraceEvent(throwable);
        Plugins.callEvent(event);
        handle(event);
    }

    public static void handle(StackTraceEvent e) {
        Configuration config = YPlugin.getInstance().getConfiguration();
        Set<Player> debuggingPlayers = Players.getAllDebugging();
        Throwable eventException = e.getException();
        if (config.enableStackTraceBook()) {
            ItemStack exceptionBook = Debugger.createExceptionBook(eventException);
            debuggingPlayers.forEach(p -> Players.giveItem(p, exceptionBook));
        }
        if (config.enableStackTraceChat()) {
            String[] exceptionMessages = Messages.exceptionInfo(eventException);
            debuggingPlayers.forEach(p -> Chat.message(p.getPlayer(), exceptionMessages));
        }
        Chat.messageConsole(Messages.exceptionInfo(eventException));
    }
}
