package io.github.thewebcode.yplugin.gui;

import io.github.thewebcode.yplugin.gui.builder.gui.PaginatedBuilder;
import io.github.thewebcode.yplugin.gui.builder.gui.ScrollingBuilder;
import io.github.thewebcode.yplugin.gui.builder.gui.SimpleBuilder;
import io.github.thewebcode.yplugin.gui.builder.gui.StorageBuilder;
import io.github.thewebcode.yplugin.gui.components.GuiType;
import io.github.thewebcode.yplugin.gui.components.InteractionModifier;
import io.github.thewebcode.yplugin.gui.components.ScrollType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class Gui extends BaseGui{
    public Gui(final int rows, @NotNull final String title, @NotNull final Set<InteractionModifier> interactionModifiers) {
        super(rows, title, interactionModifiers);
    }

    public Gui(@NotNull final GuiType guiType, @NotNull final String title, @NotNull final Set<InteractionModifier> interactionModifiers) {
        super(guiType, title, interactionModifiers);
    }

    @Deprecated
    public Gui(final int rows, @NotNull final String title) {
        super(rows, title);
    }

    @Deprecated
    public Gui(@NotNull final String title) {
        super(1, title);
    }

    @Deprecated
    public Gui(@NotNull final GuiType guiType, @NotNull final String title) {
        super(guiType, title);
    }

    @NotNull
    @Contract("_ -> new")
    public static SimpleBuilder gui(@NotNull final GuiType type) {
        return new SimpleBuilder(type);
    }

    @NotNull
    @Contract(" -> new")
    public static SimpleBuilder gui() {
        return gui(GuiType.CHEST);
    }

    @NotNull
    @Contract(" -> new")
    public static StorageBuilder storage() {
        return new StorageBuilder();
    }

    @NotNull
    @Contract(" -> new")
    public static PaginatedBuilder paginated() {
        return new PaginatedBuilder();
    }

    @NotNull
    @Contract("_ -> new")
    public static ScrollingBuilder scrolling(@NotNull final ScrollType scrollType) {
        return new ScrollingBuilder(scrollType);
    }

    @NotNull
    @Contract(" -> new")
    public static ScrollingBuilder scrolling() {
        return scrolling(ScrollType.VERTICAL);
    }
}
