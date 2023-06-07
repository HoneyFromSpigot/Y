package io.github.thewebcode.y.gui;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WTextField;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.thewebcode.y.networking.ModMessages;
import io.github.thewebcode.y.networking.packet.HandshakeC2SPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class LoginGuiDescription extends LightweightGuiDescription {
    public LoginGuiDescription(){
        WGridPanel panel = new WGridPanel();
        setRootPanel(panel);
        rootPanel.setSize(180, 100);
        setTitleAlignment(HorizontalAlignment.CENTER);

        WTextField passwordinput = new WTextField();
        passwordinput.setMaxLength(16);
        passwordinput.setSuggestion(Text.of("Password"));
        panel.add(passwordinput, 1, 1, 8, 1);

        WButton button = new WButton(Text.of("Login"));
        button.setAlignment(HorizontalAlignment.CENTER);
        panel.add(button, 1, 3, 8, 1);

        button.setOnClick(() -> {
            String text = passwordinput.getText();
            ClientPlayNetworking.send(ModMessages.HANDSHAKE_C2S, new HandshakeC2SPacket(MinecraftClient.getInstance().player.getName().getString(), text).value());
            MinecraftClient.getInstance().currentScreen.close();
        });

    }
}
