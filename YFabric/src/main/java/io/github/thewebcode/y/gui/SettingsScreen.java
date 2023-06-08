package io.github.thewebcode.y.gui;

import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import net.minecraft.text.Text;

public class SettingsScreen extends CottonClientScreen {
    public SettingsScreen(SettingsGuiDescription description) {
        super(Text.of("Settings"), description);
    }
}
