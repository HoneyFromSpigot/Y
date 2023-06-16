package io.github.thewebcode.y.gui;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.VerticalAlignment;
import io.github.thewebcode.y.YFabricMod;
import io.github.thewebcode.y.gui.widget.YToggleButton;
import io.github.thewebcode.y.networking.ModMessages;
import io.github.thewebcode.y.networking.packet.SettingUpdateC2SPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.util.HashMap;

public class SettingsGuiDescription extends LightweightGuiDescription {
    public SettingsGuiDescription(HashMap<String, String> settingsMap) {
        WGridPanel panel = new WGridPanel();
        setRootPanel(panel);
        rootPanel.setSize(300, 220);
        setTitleAlignment(HorizontalAlignment.CENTER);

        WGridPanel tallPanel = new WGridPanel();
        tallPanel.setLocation(0, 0);
        tallPanel.setSize(500, settingsMap.size());

        WScrollPanel scrollPanel = new WScrollPanel(tallPanel);
        scrollPanel.setScrollingHorizontally(TriState.FALSE);
        scrollPanel.setScrollingVertically(TriState.TRUE);

        HashMap<String, WWidget> widgetMap = new HashMap<>();

        int keyYPos = 1;
        for (String key : settingsMap.keySet()) {
            WLabel label = new WLabel(Text.of(key));
            label.setVerticalAlignment(VerticalAlignment.BOTTOM);
            label.setHorizontalAlignment(HorizontalAlignment.LEFT);
            tallPanel.add(label, 0, keyYPos, 5, 1);

            String value = settingsMap.get(key);

            String dataType = getDataType(value);

            switch (dataType.toLowerCase()){
                case "string":
                    WTextField textField = new WTextField();
                    textField.setMaxLength(200);
                    textField.setText(value);
                    tallPanel.add(textField, 8, keyYPos, 5, 1);
                    widgetMap.put(key, textField);
                    break;
                case "boolean":
                    YToggleButton toggleButton = new YToggleButton();
                    toggleButton.setToggle(value.equalsIgnoreCase("true"));
                    tallPanel.add(toggleButton, 8, keyYPos, 5, 1);
                    widgetMap.put(key, toggleButton);
                    break;
                case "double", "int":
                    WTextField tf = new WTextField();
                    tf.setMaxLength(200);
                    tf.setText(value);
                    tallPanel.add(tf, 8, keyYPos, 2, 1);
                    widgetMap.put(key, tf);
                    break;
            }
            keyYPos++;
        }


        WLabel copyRight = new WLabel(Text.of("Developed by TheWebcode"));
        copyRight.setVerticalAlignment(VerticalAlignment.BOTTOM);
        copyRight.setHorizontalAlignment(HorizontalAlignment.CENTER);
        tallPanel.add(copyRight, 5, keyYPos, 5, 2);

        panel.add(scrollPanel, 1, 1, 15, 8);
        tallPanel.setLocation(0, 0);

        WButton exit = new WButton(Text.of("Save & Exit"));
        exit.setOnClick(() -> {
            HashMap<String, String> updatedSettingsMap = new HashMap<>();
            widgetMap.keySet().forEach(key -> {
                WWidget widget = widgetMap.get(key);
                if(widget instanceof WTextField){
                    WTextField textfiled = (WTextField) widget;
                    String text = textfiled.getText();
                    updatedSettingsMap.put(key, text);
                } else if(widget instanceof YToggleButton){
                    YToggleButton toggleButton = (YToggleButton) widget;
                    String toggle = toggleButton.getToggle() ? "true" : "false";
                    updatedSettingsMap.put(key, toggle);
                }
            });

            MinecraftClient.getInstance().currentScreen.close();
            MinecraftClient.getInstance().execute(() -> {
                StringBuilder sb = new StringBuilder();
                String RSK = YFabricMod.REMOTE_SERVER_KEY;
                sb.append("\\{" + MinecraftClient.getInstance().player.getName().getString() + "}");
                sb.append("\\{" + RSK + "}");

                updatedSettingsMap.keySet().forEach(key -> {
                    String settingPart = "{" + key + "|" + updatedSettingsMap.get(key).replace("§", "&") + "}";
                    sb.append(settingPart);
                });

                String fullSettings = sb.toString();

                System.out.println("Sending settings: " + fullSettings);
                ClientPlayNetworking.send(ModMessages.UPDATE_SETTINGS, new SettingUpdateC2SPacket(fullSettings).value());
            });
        });
        panel.add(exit, 10, 10, 6, 1);

        WButton cancel = new WButton(Text.of("Cancel"));
        panel.add(cancel, 1, 10, 6, 1);
        cancel.setOnClick(() -> {
            MinecraftClient.getInstance().currentScreen.close();
        });
    }
    
    private static String getKeyByValue(String value, HashMap<String, String> settingsmap){
        for (String key : settingsmap.keySet()) {
            if (settingsmap.get(key).equals(value)) {
                return key;
            }
        }
        return null;
    }

    private static String getDataType(String s){
        if(s.contains("true") || s.contains("false")) return "boolean";
        if(s.contains(".")) return "double";

        try{
            Integer.parseInt(s);
            return "int";
        } catch (NumberFormatException ignore){
        }

        return "string";
    }
}
