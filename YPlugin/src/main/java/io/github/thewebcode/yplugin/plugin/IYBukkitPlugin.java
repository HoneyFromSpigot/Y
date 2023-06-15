package io.github.thewebcode.yplugin.plugin;


import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.command.CommandHandler;
import io.github.thewebcode.yplugin.debug.DebugAction;
import io.github.thewebcode.yplugin.debug.Debugger;
import io.github.thewebcode.yplugin.game.gadget.Gadget;
import io.github.thewebcode.yplugin.game.gadget.Gadgets;
import io.github.thewebcode.yplugin.item.ItemMessage;
import io.github.thewebcode.yplugin.player.Players;
import io.github.thewebcode.yplugin.scoreboard.BoardManager;
import io.github.thewebcode.yplugin.scoreboard.ScoreboardManager;
import io.github.thewebcode.yplugin.threading.RunnableManager;
import io.github.thewebcode.yplugin.threading.executors.BukkitExecutors;
import io.github.thewebcode.yplugin.threading.executors.BukkitScheduledExecutorService;
import io.github.thewebcode.yplugin.utilities.ListUtils;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginLogger;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;
import java.util.logging.Logger;


public abstract class IYBukkitPlugin extends JavaPlugin implements IYPlugin {

    private BukkitScheduledExecutorService syncExecuter;

    private BukkitScheduledExecutorService asyncExecuter;

    private BoardManager scoreboardManager;

    private RunnableManager threadManager;

    private ItemMessage itemMessage;

    private Logger logger = null;

    private CommandHandler commandHandler;

    public void onEnable() {
        initLogger();
        commandHandler = new CommandHandler(this);
        threadManager = new RunnableManager(this);
        scoreboardManager = new ScoreboardManager(this, 15l);
        syncExecuter = BukkitExecutors.newSynchronous(this);
        asyncExecuter = BukkitExecutors.newAsynchronous(this);

        if (Plugins.hasProtocolLib()) {
            itemMessage = new ItemMessage(this);
        }
        if (!Plugins.hasDataFolder(this)) {
            Plugins.makeDataFolder(this);
        }
        initConfig();
        startup();
    }

    public void onDisable() {
        threadManager.cancelTasks();
        shutdown();
        Plugins.unregisterHooks(this);
    }


    public abstract void startup();

    public abstract void shutdown();

    @Override
    public String getVersion() {
        return getDescription().getVersion();
    }

    public String getAuthor() {
        return ListUtils.implode(",", getDescription().getAuthors());
    }

    public abstract void initConfig();

    public void registerCommands(Object... commands) {
        commandHandler.registerCommands(commands);
    }

    public void registerCommandsByPackage(String pkg) {
        commandHandler.registerCommandsByPackage(pkg);
    }

    public void registerListeners(Listener... listeners) {
        Plugins.registerListeners(this, listeners);
    }

    public void registerGadgets(Gadget... gadgets) {
        for (Gadget gadget : gadgets) {
            Gadgets.registerGadget(gadget);
        }
    }

    public void registerDebugActions(DebugAction... actions) {
        Debugger.addDebugAction(actions);
    }

    public void registerDebugActionsByPackage(String pkg) {
        Debugger.addDebugActionsByPackage(pkg);
    }

    public BukkitScheduledExecutorService getSyncExecuter() {
        return syncExecuter;
    }

    public BukkitScheduledExecutorService getAsyncExecuter() {
        return asyncExecuter;
    }

    public RunnableManager getThreadManager() {
        return threadManager;
    }

    public ItemMessage getItemMessage() {
        return itemMessage;
    }

    public void debug(String... message) {
        Chat.messageAll(Players.getAllDebugging(), message);
        for (String m : message) {
            logger.log(Level.INFO, m);
        }
    }

    public BoardManager getScoreboardManager() {
        return scoreboardManager;
    }

    public Logger getPluginLogger() {
        return logger;
    }

    protected void initLogger() {
        if (logger != null) {
            return;
        }
        logger = new PluginLogger(this);
        logger.setUseParentHandlers(true);
    }
}