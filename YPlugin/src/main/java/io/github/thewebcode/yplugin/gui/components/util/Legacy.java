package io.github.thewebcode.yplugin.gui.components.util;

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class Legacy {
    public static final LegacyComponentSerializer SERIALIZER = LegacyComponentSerializer.builder()
            .hexColors()
            .useUnusualXRepeatedCharacterHexFormat()
            .build();

    private Legacy() {
        throw new UnsupportedOperationException("Class should not be instantiated!");
    }
}
