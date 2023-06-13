package io.github.thewebcode.y.gui.widget;

import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.widget.WToggleButton;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

public class YToggleButton extends WToggleButton {

    @Override
    public void paint(DrawContext context, int x, int y, int mouseX, int mouseY) {
        ScreenDrawing.texturedRect(context, x, y, 18, 29, isOn ? onImage : offImage, 0xFFFFFFFF);
        if (isFocused()) {
            ScreenDrawing.texturedRect(context, x, y, 18, 24, focusImage, 0xFFFFFFFF);
        }

        if (label!=null) {
            ScreenDrawing.drawString(context, label.asOrderedText(), x + 22, y+6, shouldRenderInDarkMode() ? darkmodeColor : color);
        }
        super.paint(context, x, y, mouseX, mouseY);
    }

    /*
    @Deprecated
    public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
        ScreenDrawing.texturedRect(matrices, x, y, 18, 29, isOn ? onImage : offImage, 0xFFFFFFFF);
        if (isFocused()) {
            ScreenDrawing.texturedRect(matrices, x, y, 18, 24, focusImage, 0xFFFFFFFF);
        }

        if (label!=null) {
            ScreenDrawing.drawString(matrices, label.asOrderedText(), x + 22, y+6, shouldRenderInDarkMode() ? darkmodeColor : color);
        }
    }

     */
}
