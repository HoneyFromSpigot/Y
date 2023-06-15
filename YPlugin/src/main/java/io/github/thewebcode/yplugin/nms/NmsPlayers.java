package io.github.thewebcode.yplugin.nms;

import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.reflection.ReflectionUtilities;
import org.bukkit.entity.Player;
import org.joor.Reflect;

import java.lang.reflect.Method;

public class NmsPlayers {

    public static void sendPacket(Player player, Object packet) {
        Method sendPacket = ReflectionUtilities.getMethod(ReflectionUtilities.getNMSClass("PlayerConnection"), "sendPacket", ReflectionUtilities.getNMSClass
                ("Packet"));
        Object playerConnection = getConnection(player);

        try {
            sendPacket.invoke(playerConnection, packet);
        } catch (Exception e) {
            Chat.debug("Failed to send a packet to: " + player.getName());
            e.printStackTrace();
        }
    }

    public static Object toEntityPlayer(Player player) {
        Method getHandle = ReflectionUtilities.getMethod(player.getClass(), "getHandle");
        try {
            return getHandle.invoke(player);
        } catch (Exception e) {
            Chat.debug("Failed retrieve the NMS Player-Object of:" + player.getName());
            return null;
        }
    }

    public static Object getConnection(Player player) {
        return ReflectionUtilities.getField(ReflectionUtilities.getNMSClass("EntityPlayer"), "playerConnection", toEntityPlayer(player));
    }

    public static Object getNetworkManager(Player player) {
        try {
            return ReflectionUtilities.getField(getConnection(player).getClass(), "networkManager").get(getConnection(player));
        } catch (IllegalAccessException e) {
            Chat.debug("Failed to get the NetworkManager of player: " + player.getName());
            return null;
        }
    }

    public static void setContainerDefault(Player player) {
        Object entityPlayer = toEntityPlayer(player);
        Reflect.on(entityPlayer).set("activeContainer",Reflect.on(entityPlayer).get("defaultContainer"));
    }

    public static void setActiveContainer(Player player, Object container) {
        Object entityPlayer = toEntityPlayer(player);
        Class containerClass = ReflectionUtilities.getNMSClass("Container");

        Reflect.on(entityPlayer).set("activeContainer",containerClass.cast(container));

    }

    public void closeInventory(Player player) {
        Class craftEventFactory = ReflectionUtilities.getCBClass("event.CraftEventFactory");

        Reflect.on(craftEventFactory).call("handleInventoryCloseEvent",NmsPlayers.toEntityPlayer(player));
    }


}
