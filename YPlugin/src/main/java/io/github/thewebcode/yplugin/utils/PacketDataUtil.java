package io.github.thewebcode.yplugin.utils;

import io.github.thewebcode.yplugin.YPlugin;
import io.github.thewebcode.yplugin.nms.NMSPlayer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.protocol.game.PacketPlayInCustomPayload;
import net.minecraft.network.protocol.game.PacketPlayOutCustomPayload;
import net.minecraft.resources.MinecraftKey;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.nio.charset.StandardCharsets;

public class PacketDataUtil {
    public static void handlePacket(String name, PacketPlayInCustomPayload packet){
        ByteBuf byteBuf = getByteBuf(packet);
        switch (name){
            case "handshake_c2s":
                String values = readBufAsString(byteBuf);
                String[] valueAr = values.split("\\|");
                String playerName = valueAr[0].replaceAll(" ", "").replaceAll("\\W", "");
                String pass = valueAr[1].replaceAll(" ", "").replaceAll("\\W", "");
                String masterPass = FileService.get().getConfig().getString("master-password").replace(" ", "");
                Player bukkitPlayer = Bukkit.getPlayer(playerName.replaceAll(" ", ""));

                if(masterPass.equalsIgnoreCase(pass)){
                    if(bukkitPlayer == null) return;

                    boolean needOp = new ServerSettingService.SettingReader<Boolean>().get(ServerSettingService.ServerSetting.OPERATOR_FOR_SETTINGS);

                    if(needOp && !bukkitPlayer.isOp()){
                        bukkitPlayer.sendMessage("§c§lYMod §r§7» §cYou are not an operator!");
                        return;
                    }

                    LoggingService.warning(playerName + " has logged in with the master password! Sending RSK...");

                    String serverRemoteKey = EncryptionUtil.randomCase(EncryptionUtil.genPassword(20));
                    YPlugin.getInstance().getRemoteSessionManager().addKey(bukkitPlayer.getName(), serverRemoteKey);

                    ByteBuf buf = Unpooled.buffer();
                    buf.writeBytes(serverRemoteKey.getBytes());
                    PacketPlayOutCustomPayload handshakePayload = new PacketPlayOutCustomPayload(new MinecraftKey("yplugin", "handshake_s2c"), new PacketDataSerializer(buf));

                    ByteBuf settingsbuffer = Unpooled.buffer();
                    String settings = YPlugin.getInstance().getServerSettingService().getServerSettingsAsString();
                    settingsbuffer.writeBytes(settings.getBytes(StandardCharsets.UTF_8));
                    PacketPlayOutCustomPayload openSettingsPayload = new PacketPlayOutCustomPayload(new MinecraftKey("yplugin", "open_settings"), new PacketDataSerializer(settingsbuffer));

                    NMSPlayer nmsPlayer = NMSUtil.getNMSPlayer(bukkitPlayer);
                    nmsPlayer.sendPacket(handshakePayload);
                    nmsPlayer.sendPacket(openSettingsPayload);

                    bukkitPlayer.sendMessage("§c§lYMod §r§7» §aYou have logged in with the master password!");
                }else bukkitPlayer.sendMessage("§c§lYMod §r§7» §cIncorrect password! Please try again!");
                break;
            case "hello":
                break;
            case "update_settings":
                String packetValue = readBufAsString(byteBuf);

                String[] split = packetValue.split("}");
                String playerName1 = split[0].substring(1).replaceAll("\\W", "");
                String rsk = split[1].replaceAll("\\W", "");

                Player sendingPlayer = Bukkit.getPlayer(playerName1);

                if(sendingPlayer == null) {
                    LoggingService.severe("Got RSK on update_settings, but player (" + playerName1 + ") is null!");
                    return;
                }

                boolean keyValid = YPlugin.getInstance().getRemoteSessionManager().isKeyValid(sendingPlayer.getName(), rsk);

                if(!keyValid){
                    sendingPlayer.sendMessage("§c§lYMod §r§7» §cYour RSK is invalid! Please login to the Settings Panel again!");
                    return;
                }

                for (int i = 2; i < split.length; i++) {
                    String settings = split[i].replaceAll("\\{", "");

                    String[] settingSplit = settings.split("\\|");
                    String settingName = settingSplit[0].replaceAll("[^a-zA-Z0-9\\s]", "");
                    String settingValue = settingSplit[1].replaceAll("[^a-zA-Z0-9\\s]", "");

                    ServerSettingService.ServerSetting serverSetting = ServerSettingService.ServerSetting.getByName(settingName);

                    if(serverSetting == null){
                        LoggingService.severe("Got invalid setting name on update_settings: " + settingName);
                        continue;
                    }

                    new ServerSettingService.SettingWriter<String>().write(serverSetting, settingValue);
                }

                sendingPlayer.sendMessage(LanguageService.get(LanguageService.Language.DEFAULT, LanguageService.MessageKey.SETTINGS_UPDATED_SUCCESS, true));
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
