package io.github.thewebcode.yplugin;

import io.github.thewebcode.yplugin.command.YLoginCommand;
import io.github.thewebcode.yplugin.event.Eventlistener;
import io.github.thewebcode.yplugin.utils.FileService;
import io.github.thewebcode.yplugin.utils.LanguageService;
import io.github.thewebcode.yplugin.utils.LoggingService;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class YPlugin extends JavaPlugin {
    private static YPlugin instance;

    private FileService fileService;
    private LoggingService loggingService;
    private LanguageService languageService;

    @Override
    public void onEnable() {
        instance = this;
        this.loggingService = new LoggingService();
        this.fileService = new FileService();
        this.languageService = new LanguageService();

        registerCommands();
        registerEvents();

        LoggingService.info(LanguageService.get(LanguageService.Language.EN, LanguageService.MessageKey.PLUGIN_ENABLED));
    }

    @Override
    public void onDisable() {
        this.loggingService.close();
    }

    private void registerCommands(){
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

    public static YPlugin getInstance() {
        return instance;
    }
}
