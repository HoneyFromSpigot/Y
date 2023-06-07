package io.github.thewebcode.yplugin.utils;

import io.github.thewebcode.yplugin.YPlugin;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class LanguageService {
    private final String FILE_URL = "https://raw.githubusercontent.com/TheWebcode/Y/main/YPlugin/src/main/resources/messages.yml";
    private File languageFile;
    private YamlConfiguration languageConfig;

    public LanguageService(){
        LoggingService.info("Starting LanguageService...");

        this.languageFile = new File(FileService.get().getFolder(), "messages.yml");

        try{
            if(!languageFile.exists()){
                LoggingService.warning("Language file not found! Downloading...");
                FileService.get().download(FILE_URL, languageFile);
            }
        } catch(Exception e){
            e.printStackTrace();
        }

        this.languageConfig = YamlConfiguration.loadConfiguration(languageFile);
        LoggingService.info("LanguageService successfully started!");
    }

    public String get(String key){
        return languageConfig.getString(key);
    }

    public static String get(Language language, String key){
        //TODO: Set default language
        return get().get(language.getPrefix() + "." + key);
    }

    public static String get(Language language, MessageKey key){
        //TODO: Set default language
        return get(language, key.getKey());
    }

    public static LanguageService get(){
        return YPlugin.getInstance().getLanguageService();
    }

    public static enum Language{
        DEFAULT("default"),
        DE("de"),
        EN("en");

        private String prefix;

        Language(String prefix){
            this.prefix = prefix;
        }

        public String getPrefix() {
            return prefix;
        }
    }

    public static enum MessageKey{
        HELLO("default.hello"),
        COMMAND_PLAYER_ONLY("command.player_only"),
        PLUGIN_ENABLED("default.plugin_enabled"),
        PLUGIN_DISABLED("default.plugin_disabled");
        private String key;

        MessageKey(String key){
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }
}
