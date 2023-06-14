package io.github.thewebcode.yplugin.gui.components;

import java.util.List;

public interface Serializable {
    List<String> encodeGui();
    void decodeGui(final List<String> gui);
}
