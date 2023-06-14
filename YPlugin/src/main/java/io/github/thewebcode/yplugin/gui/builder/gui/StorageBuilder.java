package io.github.thewebcode.yplugin.gui.builder.gui;

import io.github.thewebcode.yplugin.gui.StorageGui;
import io.github.thewebcode.yplugin.gui.components.util.Legacy;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class StorageBuilder extends BaseGuiBuilder<StorageGui, StorageBuilder> {
    @NotNull
    @Override
    @Contract(" -> new")
    public StorageGui create() {
        final StorageGui gui = new StorageGui(getRows(), Legacy.SERIALIZER.serialize(getTitle()), getModifiers());

        final Consumer<StorageGui> consumer = getConsumer();
        if (consumer != null) consumer.accept(gui);

        return gui;
    }
}
