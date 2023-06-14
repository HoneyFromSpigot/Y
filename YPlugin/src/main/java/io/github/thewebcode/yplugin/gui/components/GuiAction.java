package io.github.thewebcode.yplugin.gui.components;

import org.bukkit.event.Event;

public interface GuiAction<T extends Event> {
    void execute(final T event);
}
