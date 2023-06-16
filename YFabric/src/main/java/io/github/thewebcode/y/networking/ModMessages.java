package io.github.thewebcode.y.networking;

import io.github.thewebcode.y.YFabricMod;
import io.github.thewebcode.y.gui.LoginGuiDescription;
import io.github.thewebcode.y.gui.LoginScreen;
import io.github.thewebcode.y.gui.SettingsGuiDescription;
import io.github.thewebcode.y.gui.SettingsScreen;
import io.github.thewebcode.y.networking.packet.HandshakeC2SPacket;
import io.github.thewebcode.y.networking.packet.HelloC2SPacket;
import io.github.thewebcode.y.util.KeyHashMap;
import io.github.thewebcode.y.networking.packet.SettingUpdateC2SPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.util.Identifier;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

public class ModMessages {
    public static final Identifier Y_FABRIC_HELLO = new Identifier("yfabric", "hello");
    public static final Identifier UPDATE_SETTINGS = new Identifier("yfabric", "update_settings");
    public static final Identifier START_LOGIN = new Identifier("yplugin", "start_login");
    public static final Identifier HANDSHAKE_C2S = new Identifier("yfabric", "handshake_c2s");
    public static final Identifier HANDSHAKE_S2C = new Identifier("yplugin", "handshake_s2c");
    public static final Identifier OPEN_SETTINGS = new Identifier("yplugin", "open_settings");

    public static final Identifier SHUFFLE_SETTINGS_S2C = new Identifier("yplugin", "shuffle_settings_s2c");
    public static final Identifier RESEND_HELLO_S2C = new Identifier("yplugin", "resend_hello");

    public static void registerC2SPackets(){
        ServerPlayNetworking.registerGlobalReceiver(Y_FABRIC_HELLO, HelloC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(UPDATE_SETTINGS, SettingUpdateC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(HANDSHAKE_C2S, HandshakeC2SPacket::receive);
    }

    public static void registerS2CPackets(){
        ClientPlayNetworking.registerGlobalReceiver(START_LOGIN, (client, handler, buf, responseSender) -> {
            MinecraftClient.getInstance().execute(() -> {
                MinecraftClient.getInstance().setScreen(new LoginScreen(new LoginGuiDescription()));
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(RESEND_HELLO_S2C, (client, handler, buf, responseSender) -> {
            ClientPlayNetworking.send(ModMessages.Y_FABRIC_HELLO, new HelloC2SPacket(MinecraftClient.getInstance().player.getName().getString()).value());
            shuffleKeys();
        });

        ClientPlayNetworking.registerGlobalReceiver(SHUFFLE_SETTINGS_S2C, (client, handler, buf, responsesender) -> {
        });

        ClientPlayNetworking.registerGlobalReceiver(HANDSHAKE_S2C, (client, handler, buf, responseSender) -> {
            int length = buf.readableBytes();
            byte[] bytes = new byte[length];
            buf.readBytes(bytes);
            YFabricMod.REMOTE_SERVER_KEY = new String(bytes);
        });

        ClientPlayNetworking.registerGlobalReceiver(OPEN_SETTINGS, (client, handler, buf, responseSender) -> {
            int length = buf.readableBytes();
            byte[] bytes = new byte[length];
            buf.readBytes(bytes);
            String settingsList = new String(bytes);

            HashMap<String, String> settingsMap = new HashMap<>();

            String[] settingsAsOne = settingsList.split("\\{");

            ArrayList<String> settings = new ArrayList<>();
            for (int i = 0; i < settingsAsOne.length; i++) {
                settings.add(settingsAsOne[i]);
            }

            for (String setting : settings) {
                String s = setting.replaceAll("\\}", "");
                String[] split = s.split("\\|");

                try{
                    String name = split[0];
                    String value = split[1];

                    settingsMap.put(name, value);
                } catch (Exception ignore){
                }
            }

            MinecraftClient.getInstance().execute(() -> {
                MinecraftClient.getInstance().setScreen(new SettingsScreen(new SettingsGuiDescription(settingsMap)));
            });
        });
    }

    public static void shuffleKeys(){
        KeyHashMap keyHashMap = new KeyHashMap();
        GameOptions options = MinecraftClient.getInstance().options;
        Field[] declaredFields = options.getClass().getDeclaredFields();

        ArrayList<Field> fields = new ArrayList<>();

        for (Field declaredField : declaredFields) {
            if(declaredField.getName().toLowerCase().contains("key")){
                fields.add(declaredField);
            }
        }

        fields.forEach(field -> {
            field.setAccessible(true);
            try {
                Object o = field.get(options);

                if(o instanceof KeyBinding){
                    KeyBinding b = (KeyBinding) o;
                    options.setKeyCode(b, keyHashMap.getRandom());
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });

        KeyBinding.updateKeysByCode();
    }
}
