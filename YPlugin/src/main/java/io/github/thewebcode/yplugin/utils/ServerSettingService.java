package io.github.thewebcode.yplugin.utils;

import io.github.thewebcode.yplugin.YPlugin;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ServerSettingService {
    private File settingsFile;
    private YamlConfiguration settings;

    public ServerSettingService(){
        settingsFile = new File(FileService.get().getFolder(), "settings.yml");

        try{
            if(!settingsFile.exists()) settingsFile.createNewFile();
        } catch (Exception e){
            e.printStackTrace();
        }

        settings = YamlConfiguration.loadConfiguration(settingsFile);
    }

    public String getServerSettingsAsString(){
        StringBuilder b = new StringBuilder();
        for (ServerSetting setting : ServerSetting.values()){
            String serverSettingAsString = getServerSettingAsString(setting);
            b.append(serverSettingAsString);
        }

        return b.toString();
    }

    public String getServerSettingAsString(ServerSetting setting){
        Object value = new SettingReader<Object>().get(setting);
        String settingName = setting.getName();
        return  "{" + settingName + "|" + value + "}";
    }

    public YamlConfiguration getSettings() {
        return settings;
    }

    public File getSettingsFile() {
        return settingsFile;
    }

    public static class SettingReader<T>{
        public T get(ServerSetting setting){
            String key = setting.getKey();

            YamlConfiguration settings1 = YPlugin.getInstance().getServerSettingService().getSettings();
            if(!settings1.contains(key)) {
                settings1.set(key, setting.getDefaultValue());
                FileService.get().save(settings1, YPlugin.getInstance().getServerSettingService().getSettingsFile());
                return (T) setting.getDefaultValue();
            }

            Object o = settings1.get(key);
            if(o == null) return (T) setting.getDefaultValue();
            return (T) o;
        }
    }

    public static enum ServerSetting{
        Y_MOD_REQUIRED("Y Mod Required", "y_mod_required", true),
        TEST("Test", "test", "1");

        private String name;
        private String key;
        private Object defaultValue;

        private ServerSetting(String name, String key, Object defaultValue){
            this.name = name;
            this.key = key;
            this.defaultValue = defaultValue;
        }

        public String getKey() {
            return key;
        }

        public String getName() {
            return name;
        }

        public Object getDefaultValue() {
            return defaultValue;
        }

        public static ServerSetting getByName(String name){
            for(ServerSetting setting : values()){
                if(setting.getName().equalsIgnoreCase(name)) return setting;
            }
            return null;
        }

        public static ServerSetting getByKey(String key){
            for(ServerSetting setting : values()){
                if(setting.getKey().equalsIgnoreCase(key)) return setting;
            }
            return null;
        }
    }
}
