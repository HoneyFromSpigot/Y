package io.github.thewebcode.yplugin.utils;

import io.github.thewebcode.yplugin.YPlugin;
import io.github.thewebcode.yplugin.nms.NMSPlayer;
import io.github.thewebcode.yplugin.nms.NMSPlayer_v1_20_R1;
import org.bukkit.entity.Player;

public class NMSUtil {
    public static String getServerVersion(){
        return YPlugin.SERVER_VERSION;
    }

    public static NMSPlayer getNMSPlayer(Player player){
        switch (getServerVersion()){
            case "v1_20_R1":
                return new NMSPlayer_v1_20_R1(player);
            default:
                return null;
        }
    }
}
