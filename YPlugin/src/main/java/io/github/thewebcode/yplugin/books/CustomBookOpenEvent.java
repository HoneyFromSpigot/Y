package io.github.thewebcode.yplugin.books;

import io.github.thewebcode.yplugin.inventory.HandSlot;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class CustomBookOpenEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    @Getter
    @Setter
    private boolean cancelled;

    @Getter
    private final Player player;

    @Getter
    @Setter
    private HandSlot hand;

    @Getter
    @Setter
    private ItemStack book;

    public CustomBookOpenEvent(Player player, ItemStack book, boolean offHand) {
        this.player = player;
        this.book = book;
        this.hand = offHand ? HandSlot.OFF_HAND : HandSlot.MAIN_HAND;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
