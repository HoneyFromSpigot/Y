package io.github.thewebcode.yplugin;

import io.github.thewebcode.yplugin.command.SettingsCommand;
import io.github.thewebcode.yplugin.command.YLoginCommand;
import io.github.thewebcode.yplugin.event.Eventlistener;
import io.github.thewebcode.yplugin.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class YPlugin extends JavaPlugin {
    public static String SERVER_VERSION = "";
    private static YPlugin instance;

    private FileService fileService;
    private LoggingService loggingService;
    private LanguageService languageService;
    private RemoteSessionManager remoteSessionManager;
    private ServerSettingService serverSettingService;

    @Override
    public void onEnable() {
        instance = this;
        this.loggingService = new LoggingService();

        registerNMS();

        this.fileService = new FileService();
        this.languageService = new LanguageService();
        this.remoteSessionManager = new RemoteSessionManager();
        this.serverSettingService = new ServerSettingService();

        registerCommands();
        registerEvents();

        LoggingService.info(LanguageService.get(LanguageService.Language.EN, LanguageService.MessageKey.PLUGIN_ENABLED));
    }

    @Override
    public void onDisable() {
        this.loggingService.close();
    }

    private void registerNMS(){
        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        SERVER_VERSION = packageName.split("\\.")[3];

        LoggingService.info("Starting up on Server version: " + SERVER_VERSION);
    }

    private void registerCommands(){
        getCommand("settings").setExecutor(new SettingsCommand());
        getCommand("login").setExecutor(new YLoginCommand());
    }

    private void registerEvents(){
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new Eventlistener(), this);
    }

    public LoggingService getLoggingService() {
        return loggingService;
    }

    public FileService getFileService() {
        return fileService;
    }

    public LanguageService getLanguageService() {
        return languageService;
    }

    public RemoteSessionManager getRemoteSessionManager() {
        return remoteSessionManager;
    }

    public ServerSettingService getServerSettingService() {
        return serverSettingService;
    }

    public static YPlugin getInstance() {
        return instance;
    }
}
