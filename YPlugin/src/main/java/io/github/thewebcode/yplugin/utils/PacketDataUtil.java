package io.github.thewebcode.yplugin.utils;

import io.github.thewebcode.yplugin.YPlugin;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.protocol.game.PacketPlayInCustomPayload;
import net.minecraft.network.protocol.game.PacketPlayOutCustomPayload;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.server.network.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.nio.charset.StandardCharsets;

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
                    if(player1 == null) return;
                    if(!player1.isOp()){
                        player1.sendMessage("§c§lYMod §r§7» §cYou are not an operator!");
                        return;
                    }
                    LoggingService.warning(player + " has logged in with the master password! Sending RSK...");

                    String serverRemoteKey = EncryptionUtil.randomCase(EncryptionUtil.genPassword(20));
                    YPlugin.getInstance().getRemoteSessionManager().addKey(player1.getName(), serverRemoteKey);

                    ByteBuf buf = Unpooled.buffer();
                    buf.writeBytes(serverRemoteKey.getBytes());
                    PacketPlayOutCustomPayload payload = new PacketPlayOutCustomPayload(new MinecraftKey("yplugin", "handshake_s2c"), new PacketDataSerializer(buf));


                    ByteBuf settingsbuffer = Unpooled.buffer();
                    String settings = YPlugin.getInstance().getServerSettingService().getServerSettingsAsString();
                    settingsbuffer.writeBytes(settings.getBytes(StandardCharsets.UTF_8));
                    PacketPlayOutCustomPayload payload2 = new PacketPlayOutCustomPayload(new MinecraftKey("yplugin", "open_settings"), new PacketDataSerializer(settingsbuffer));

                    CraftPlayer craftPlayer = (CraftPlayer) player1;
                    PlayerConnection b = craftPlayer.getHandle().b;

                    b.a(payload);
                    b.a(payload2);

                    player1.sendMessage("§c§lYMod §r§7» §aYou have logged in with the master password!");
                }else player1.sendMessage("§c§lYMod §r§7» §cIncorrect password! Please try again!");
                break;
            case "hello":
                String playerName = readBufAsString(byteBuf);
                LoggingService.warning("YClient User:" + playerName + " has joined the server.");
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
