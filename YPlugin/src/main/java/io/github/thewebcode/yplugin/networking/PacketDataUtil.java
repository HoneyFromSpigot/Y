package io.github.thewebcode.yplugin.networking;

import io.github.thewebcode.yplugin.YPlugin;
import io.github.thewebcode.yplugin.config.Configuration;
import io.github.thewebcode.yplugin.nms.NMS;
import io.github.thewebcode.yplugin.nms.NmsPlayer;
import io.github.thewebcode.yplugin.utilities.EncryptionUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.protocol.game.PacketPlayOutCustomPayload;
import net.minecraft.resources.MinecraftKey;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;

public class PacketDataUtil {
    public static void handle(Packet packet){
        Configuration configuration = YPlugin.getInstance().getConfiguration();
        Player player = packet.getPlayer();
        String packetName = packet.getName();
        ByteBuf byteBuf = packet.getBuf();
        switch (packetName.toLowerCase()){
            case "update_settings":
                String packetValue = Packet.readBufAsString(byteBuf);

                String[] split = packetValue.split("}");
                String playerName1 = split[0].substring(1).replaceAll("\\W", "");
                String rsk = split[1].replaceAll("\\W", "");

                Player sendingPlayer = Bukkit.getPlayer(playerName1);

                if(sendingPlayer == null) return;

                //TODO: Add if key is valid
                for (int i = 2; i < split.length; i++) {
                    String settings = split[i].replaceAll("\\{", "");

                    String[] settingSplit = settings.split("\\|");
                    String settingName = settingSplit[0].replaceAll("[^&a-zA-Z0-9-\\s]", "");
                    String settingValue = settingSplit[1].replaceAll("[^&a-zA-Z0-9\\s]", "");

                    try{
                        Field declaredField = configuration.getClass().getDeclaredField(settingName);
                        declaredField.setAccessible(true);

                        try {
                            int ival = Integer.valueOf(settingValue);
                            declaredField.setInt(configuration, ival);
                        } catch (NumberFormatException ei){
                            try {
                                double dval = Double.valueOf(settingValue);
                                declaredField.setDouble(configuration, dval);
                            } catch ( NumberFormatException ed ) {
                                String lowerCase = settingValue.toLowerCase();
                                if(lowerCase.equalsIgnoreCase("true") || lowerCase.equalsIgnoreCase("false")){
                                    boolean b = Boolean.valueOf(lowerCase);
                                    declaredField.set(configuration, b);
                                } else{
                                    System.out.println("Setting value: " + settingValue);
                                    declaredField.set(configuration, settingValue.replaceAll("&", "§"));
                                }
                            }
                        }

                        declaredField.setAccessible(false);
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }

                configuration.save();

                break;
            case "handshake_c2s":
                String values = Packet.readBufAsString(byteBuf);
                String[] valueAr = values.split("\\|");
                String playerName = valueAr[0].replaceAll(" ", "").replaceAll("\\W", "");
                String pass = valueAr[1].replaceAll(" ", "").replaceAll("\\W", "");
                String masterPass = YPlugin.getInstance().getConfiguration().getMasterLoginPass();
                Player bukkitPlayer = Bukkit.getPlayer(playerName.replaceAll(" ", ""));

                if(masterPass.equalsIgnoreCase(pass)){
                    if(bukkitPlayer == null) return;
                    boolean needOp = YPlugin.getInstance().getConfiguration().needRemoteLoginOp();

                    if(needOp && !bukkitPlayer.isOp()){
                        bukkitPlayer.sendMessage("§c§lYMod §r§7» §cYou are not an operator!");
                        return;
                    }

                    String serverRemoteKey = EncryptionUtil.randomCase(EncryptionUtil.genPassword(20));

                    ByteBuf buf = Unpooled.buffer();
                    buf.writeBytes(serverRemoteKey.getBytes());
                    PacketPlayOutCustomPayload handshakePayload = new PacketPlayOutCustomPayload(new MinecraftKey("yplugin", "handshake_s2c"), new PacketDataSerializer(buf));

                    ByteBuf settingsbuffer = Unpooled.buffer();
                    StringBuilder settingsStringBuilder = new StringBuilder();
                    for (Field field : configuration.getClass().getDeclaredFields()) {
                        field.setAccessible(true);
                        try{
                            String name = field.getName();
                            String value = field.get(configuration).toString();
                            settingsStringBuilder.append("{").append(name).append("|").append(value).append("}");
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                    settingsbuffer.writeBytes(settingsStringBuilder.toString().getBytes(StandardCharsets.UTF_8));
                    PacketPlayOutCustomPayload openSettingsPayload = new PacketPlayOutCustomPayload(new MinecraftKey("yplugin", "open_settings"), new PacketDataSerializer(settingsbuffer));

                    NmsPlayer nmsPlayer = NMS.getNMSPlayer(bukkitPlayer);

                    nmsPlayer.sendPacket(handshakePayload);
                    nmsPlayer.sendPacket(openSettingsPayload);

                    bukkitPlayer.sendMessage("§c§lYMod §r§7» §aYou have logged in with the master password!");
                }else bukkitPlayer.sendMessage("§c§lYMod §r§7» §cIncorrect password! Please try again!");
                break;
            case "hello":
                YPlugin.getInstance().getLogger().info("YMod» YHello from " + player.getName() + "!");
                break;
        }
    }
}
