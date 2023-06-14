package io.github.thewebcode.yplugin.gui.builder.gui;

import io.github.thewebcode.yplugin.gui.ScrollingGui;
import io.github.thewebcode.yplugin.gui.components.ScrollType;
import io.github.thewebcode.yplugin.gui.components.util.Legacy;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class ScrollingBuilder extends BaseGuiBuilder<ScrollingGui, ScrollingBuilder> {
    private ScrollType scrollType;
    private int pageSize = 0;

    public ScrollingBuilder(@NotNull final ScrollType scrollType) {
        this.scrollType = scrollType;
    }

    @NotNull
    @Contract("_ -> this")
    public ScrollingBuilder scrollType(@NotNull final ScrollType scrollType) {
        this.scrollType = scrollType;
        return this;
    }

    @NotNull
    @Contract("_ -> this")
    public ScrollingBuilder pageSize(final int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    @NotNull
    @Override
    @Contract(" -> new")
    public ScrollingGui create() {
        final ScrollingGui gui = new ScrollingGui(getRows(), pageSize, Legacy.SERIALIZER.serialize(getTitle()), scrollType, getModifiers());

        final Consumer<ScrollingGui> consumer = getConsumer();
        if (consumer != null) consumer.accept(gui);

        return gui;
    }
}
