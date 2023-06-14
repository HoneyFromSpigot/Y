package io.github.thewebcode.yplugin.gui.builder.gui;

import io.github.thewebcode.yplugin.gui.Gui;
import io.github.thewebcode.yplugin.gui.components.GuiType;
import io.github.thewebcode.yplugin.gui.components.util.Legacy;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class SimpleBuilder extends BaseGuiBuilder<Gui, SimpleBuilder> {
    private GuiType guiType;

    public SimpleBuilder(@NotNull final GuiType guiType) {
        this.guiType = guiType;
    }

    @NotNull
    @Contract("_ -> this")
    public SimpleBuilder type(@NotNull final GuiType guiType) {
        this.guiType = guiType;
        return this;
    }

    @NotNull
    @Override
    @Contract(" -> new")
    public Gui create() {
        final Gui gui;
        final String title = Legacy.SERIALIZER.serialize(getTitle());
        if (guiType == null || guiType == GuiType.CHEST) {
            gui = new Gui(getRows(), title, getModifiers());
        } else {
            gui = new Gui(guiType, title, getModifiers());
        }

        final Consumer<Gui> consumer = getConsumer();
        if (consumer != null) consumer.accept(gui);

        return gui;
    }
}
