package io.github.thewebcode.yplugin.utils;

import io.github.thewebcode.yplugin.YPlugin;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.protocol.game.PacketPlayOutCustomPayload;
import net.minecraft.resources.MinecraftKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.io.File;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

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

    public void openSettings(Player player){
        ByteBuf settingsbuffer = Unpooled.buffer();
        String settings = YPlugin.getInstance().getServerSettingService().getServerSettingsAsString();
        settingsbuffer.writeBytes(settings.getBytes(StandardCharsets.UTF_8));
        PacketPlayOutCustomPayload payload = new PacketPlayOutCustomPayload(new MinecraftKey("yplugin", "open_settings"), new PacketDataSerializer(settingsbuffer));

        CraftPlayer craftPlayer = (CraftPlayer) player;
        craftPlayer.getHandle().c.a(payload);
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
        Object value = new SettingReader<String>().get(setting);
        String settingName = setting.getName();
        return  "{" + settingName + "|" + value + "}";
    }

    public YamlConfiguration getSettings() {
        return settings;
    }

    public File getSettingsFile() {
        return settingsFile;
    }

    public static ServerSettingService get(){
        return YPlugin.getInstance().getServerSettingService();
    }

    public static class SettingWriter<T> {
        public void write(ServerSetting setting, Object value){
            YamlConfiguration settings1 = YPlugin.getInstance().getServerSettingService().getSettings();
            settings1.set(setting.getKey(), value);
            FileService.get().save(settings1, YPlugin.getInstance().getServerSettingService().getSettingsFile());
        }
    }

    public static class SettingReader<T>{
        public T get(ServerSetting setting){
            String key = setting.getKey();

            YamlConfiguration settings1 = YPlugin.getInstance().getServerSettingService().getSettings();

            if(!settings1.contains(key)) {
                LoggingService.severe("Config did not contain " + key + "! Setting to default value!");
                new ServerSettingService.SettingWriter<String>().write(ServerSetting.OPERATOR_FOR_SETTINGS, ServerSetting.OPERATOR_FOR_SETTINGS.getDefaultValue());
                return (T) setting.getDefaultValue();
            }

            Object o = settings1.get(key);

            try {
                Class<?> clazzOfT = setting.getDefaultValue().getClass();

                switch (clazzOfT.getSimpleName().toLowerCase()) {
                    case "boolean":
                        return (T) Boolean.valueOf(o.toString());
                    case "string":
                        return (T) o.toString();
                    case "integer":
                        return (T) Integer.valueOf(o.toString());
                    case "double":
                        return (T) Double.valueOf(o.toString());
                    case "float":
                        return (T) Float.valueOf(o.toString());
                    case "long":
                        return (T) Long.valueOf(o.toString());
                    case "short":
                        return (T) Short.valueOf(o.toString());
                    case "byte":
                        return (T) Byte.valueOf(o.toString());
                    case "character":
                        return (T) Character.valueOf(o.toString().charAt(0));
                    default:
                        throw new IllegalStateException("Unexpected value: " + clazzOfT.getSimpleName().toLowerCase());
                }
            } catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }
    }

    public static enum ServerSetting{
        OPERATOR_FOR_SETTINGS("Operator for Settings", "operator_for_settings", true),
        Y_MOD_REQUIRED("Y Mod Required", "y_mod_required", true),
        TEST("Test", "test", "hello world!");

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
