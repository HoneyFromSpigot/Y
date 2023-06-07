package io.github.thewebcode.yplugin.utils;

import io.github.thewebcode.yplugin.YPlugin;
import org.apache.commons.io.FileUtils;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class FileService{
    private File FOLDER;

    public FileService(){
        this.FOLDER = new File("./plugins/y/");

        if(!FOLDER.exists()) FOLDER.mkdirs();
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


    public File getFolder() {
        return FOLDER;
    }

    public static FileService get(){
        return YPlugin.getInstance().getFileService();
    }
}
