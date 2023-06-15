package io.github.thewebcode.yplugin.nms.minecraft_1_20_R1;

import io.github.thewebcode.yplugin.nms.ActionMessageHandler;
import io.github.thewebcode.yplugin.nms.NmsPlayers;
import io.github.thewebcode.yplugin.reflection.ReflectionUtilities;
import io.github.thewebcode.yplugin.utilities.StringUtil;
import org.bukkit.entity.Player;
import org.joor.Reflect;

public class ActionMessageHandler_1_20_R1 implements ActionMessageHandler {
    @Override
    public void actionMessage(Player player, String message) {

        Object packet = Reflect.on(ReflectionUtilities.getNMSClass("PacketPlayOutChat"))
                .create(
                        Reflect.on(ReflectionUtilities.getNMSClass("ChatComponentText"))
                                .create(StringUtil.colorize(message))
                                .get(),
                        Reflect.on(ReflectionUtilities.getNMSClass("ChatMessageType")).call("valueOf", "GAME_INFO").get()
                )
                .get();

        NmsPlayers.sendPacket(player,packet);
    }
}
