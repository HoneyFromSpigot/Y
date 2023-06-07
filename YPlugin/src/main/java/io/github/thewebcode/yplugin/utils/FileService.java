package io.github.thewebcode.yplugin.utils;

import io.github.thewebcode.yplugin.YPlugin;
import org.apache.commons.io.FileUtils;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class FileService{
    private File FOLDER, configFile;
    private YamlConfiguration config;

    public FileService(){
        this.FOLDER = new File("./plugins/y/");

        if(!FOLDER.exists()) FOLDER.mkdirs();

        this.configFile = new File(FOLDER, "config.yml");
        try{
            if(!configFile.exists()) {
                configFile.createNewFile();

                this.config = YamlConfiguration.loadConfiguration(configFile);

                String pw = genPassword(7);
                config.set("master-password", pw);
                config.save(configFile);
            }
        } catch(Exception e){
            e.printStackTrace();
        }

        this.config = YamlConfiguration.loadConfiguration(configFile);
    }

    public String genPassword(int length){
        String password = "";
        for(int i = 0; i < length; i++){
            password += (char) (Math.random() * 26 + 97);
        }
        return password;
    }


    public void save(YamlConfiguration yamlConfiguration, File file){
        try{
            yamlConfiguration.save(file);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void download(String url, File file){
        try{
            FileUtils.copyURLToFile(new java.net.URL(url), file);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public YamlConfiguration getConfig() {
        return config;
    }

    public File getFolder() {
        return FOLDER;
    }

    public static FileService get(){
        return YPlugin.getInstance().getFileService();
    }
}
