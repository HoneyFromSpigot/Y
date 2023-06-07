package io.github.thewebcode.y.gui;

import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import net.minecraft.text.Text;

public class LoginScreen extends CottonClientScreen {
    public LoginScreen(LoginGuiDescription description){
        super(Text.of("Login to YServer"), description);
    }
}
