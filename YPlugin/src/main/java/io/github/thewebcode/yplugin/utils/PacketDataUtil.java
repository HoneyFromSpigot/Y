package io.github.thewebcode.yplugin.utils;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.protocol.game.PacketPlayInCustomPayload;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PacketDataUtil {
    public static void handlePacket(String name, PacketPlayInCustomPayload packet){
        ByteBuf byteBuf = getByteBuf(packet);
        switch (name){
            case "handshake_c2s":
                String values = readBufAsString(byteBuf);
                String[] valueAr = values.split("\\|");
                String player = valueAr[0].replaceAll(" ", "").replaceAll("\\W", "");
                String pass = valueAr[1].replaceAll(" ", "").replaceAll("\\W", "");
                String masterPass = FileService.get().getConfig().getString("master-password").replace(" ", "");

                Player player1 = Bukkit.getPlayer(player.replaceAll(" ", ""));

                if(masterPass.equalsIgnoreCase(pass)){
                    LoggingService.warning(player + " has logged in with the master password!");
                    player1.sendMessage("§c§lYMod §r§7» §aYou have logged in with the master password!");
                }else player1.sendMessage("§c§lYMod §r§7» §cIncorrect password! Please try again!");
                break;
            case "hello":
                String playerName = readBufAsString(byteBuf);
                LoggingService.info(playerName + " has joined the server. He is a YMod user!");
                break;
            case "update_settings":
                String packetValue = readBufAsString(byteBuf);
                String[] packetValues = packetValue.split("\\|");
                String settingName = packetValues[0];
                String settingValue = packetValues[1];

                //TODO: Update the Server Setting
                break;
        }
    }

    public static String readBufAsString(ByteBuf buf){
        int length = buf.readableBytes();
        byte[] bytes = new byte[length];
        buf.readBytes(bytes);
        return new String(bytes);
    }

    public static ByteBuf getByteBuf(PacketPlayInCustomPayload packet){
        return packet.d;
    }
}
