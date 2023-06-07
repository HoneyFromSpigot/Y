package io.github.thewebcode.yplugin.utils;

import io.github.thewebcode.yplugin.YPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;

public class RemoteSessionManager {
    private HashMap<String, String> serverKeyList;

    public RemoteSessionManager(){
        this.serverKeyList = new HashMap<>();
    }

    public void addKey(String playerName, String key){
        serverKeyList.put(playerName, key);

        new BukkitRunnable(){
            @Override
            public void run() {
                if(serverKeyList.containsKey(playerName)){
                    serverKeyList.remove(playerName);
                }
            }
        }.runTaskLater(YPlugin.getInstance(), 20 * 60 * 2);
    }

    public void remove(String playerName){
        if(serverKeyList.containsKey(playerName)){
            serverKeyList.remove(playerName);
        }
    }

    public boolean isKeyValid(String key){
        return serverKeyList.containsValue(key);
    }
}
