package io.github.thewebcode.y.gui;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.Axis;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.Texture;
import io.github.cottonmc.cotton.gui.widget.data.VerticalAlignment;
import io.github.thewebcode.y.YFabricMod;
import io.github.thewebcode.y.gui.widget.YToggleButton;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.HashMap;

public class SettingsGuiDescription extends LightweightGuiDescription {
    public SettingsGuiDescription(HashMap<String, String> settingsMap) {
        WGridPanel panel = new WGridPanel();
        setRootPanel(panel);
        rootPanel.setSize(300, 220);
        setTitleAlignment(HorizontalAlignment.CENTER);


        settingsMap.put("test1", "true");
        settingsMap.put("test2", "false");
        settingsMap.put("test3", "true");
        settingsMap.put("test4", "false");
        settingsMap.put("test5", "true");
        settingsMap.put("test6", "hello");
        settingsMap.put("test7", "world");
        settingsMap.put("test8", "1");
        settingsMap.put("test9", "2");
        settingsMap.put("test10", "3");
        settingsMap.put("test11", "3.1");
        settingsMap.put("test12", "3.2");

        WGridPanel tallPanel = new WGridPanel();
        tallPanel.setLocation(0, 0);
        tallPanel.setSize(500, settingsMap.size());

        WScrollPanel scrollPanel = new WScrollPanel(tallPanel);
        scrollPanel.setScrollingHorizontally(TriState.FALSE);
        scrollPanel.setScrollingVertically(TriState.TRUE);

        int keyYPos = 1;
        for (String key : settingsMap.keySet()) {
            WLabel label = new WLabel(Text.of(key));
            label.setVerticalAlignment(VerticalAlignment.BOTTOM);
            label.setHorizontalAlignment(HorizontalAlignment.LEFT);
            tallPanel.add(label, 0, keyYPos, 5, 1);
            keyYPos++;
        }

        int valueYPos = 1;
        for (String value : settingsMap.values()) {
            String dataType = getDataType(value);

            switch (dataType.toLowerCase()){
                case "string":
                    WTextField textField = new WTextField();
                    textField.setText(value);
                    tallPanel.add(textField, 8, valueYPos, 5, 1);
                    break;
                case "boolean":
                    YToggleButton toggleButton = new YToggleButton();
                    toggleButton.setToggle(value.equalsIgnoreCase("true"));
                    tallPanel.add(toggleButton, 8, valueYPos, 5, 1);
                    break;
                case "double", "int":
                    WTextField tf = new WTextField();
                    tf.setText(value);
                    tallPanel.add(tf, 8, valueYPos, 2, 1);
                    break;
            }

            valueYPos++;
        }


        WLabel copyRight = new WLabel(Text.of("Developed by TheWebcode"));
        copyRight.setVerticalAlignment(VerticalAlignment.BOTTOM);
        copyRight.setHorizontalAlignment(HorizontalAlignment.CENTER);
        tallPanel.add(copyRight, 5, keyYPos, 5, 2);

        panel.add(scrollPanel, 1, 1, 15, 8);
        tallPanel.setLocation(0, 0);

        WButton exit = new WButton(Text.of("Save & Exit"));
        exit.setOnClick(() -> {
            MinecraftClient.getInstance().currentScreen.close();
            //TODO: Save settings
        });
        panel.add(exit, 10, 10, 6, 1);

        WButton cancel = new WButton(Text.of("Cancel"));
        panel.add(cancel, 1, 10, 6, 1);
        cancel.setOnClick(() -> {
            MinecraftClient.getInstance().currentScreen.close();
        });
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
