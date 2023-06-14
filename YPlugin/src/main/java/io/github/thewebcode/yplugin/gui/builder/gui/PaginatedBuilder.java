package io.github.thewebcode.yplugin.gui.builder.gui;

import io.github.thewebcode.yplugin.gui.PaginatedGui;
import io.github.thewebcode.yplugin.gui.components.util.Legacy;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class PaginatedBuilder extends BaseGuiBuilder<PaginatedGui, PaginatedBuilder> {
    private int pageSize = 0;

    @NotNull
    @Contract("_ -> this")
    public PaginatedBuilder pageSize(final int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    @NotNull
    @Override
    @Contract(" -> new")
    public PaginatedGui create() {
        final PaginatedGui gui = new PaginatedGui(getRows(), pageSize, Legacy.SERIALIZER.serialize(getTitle()), getModifiers());

        final Consumer<PaginatedGui> consumer = getConsumer();
        if (consumer != null) consumer.accept(gui);

        return gui;
    }
}
