package io.github.thewebcode.yplugin.game.event;

import io.github.thewebcode.yplugin.game.gadget.Gadget;
import io.github.thewebcode.yplugin.game.gadget.Gadgets;
import io.github.thewebcode.yplugin.game.guns.BaseGun;
import io.github.thewebcode.yplugin.game.item.Weapon;
import io.github.thewebcode.yplugin.inventory.HandSlot;
import io.github.thewebcode.yplugin.plugin.Plugins;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

public class GadgetUseEvent extends Event implements Cancellable {
    public static final HandlerList handler = new HandlerList();

    private Action action;
    private HandSlot hand;

    private boolean cancelled = false;

    private Player player;
    private Gadget gadget;

    @Getter
    @Setter
    private Block block = null;

    public GadgetUseEvent(Player player, Action action, ItemStack item, HandSlot hand) {
        this.player = player;
        this.gadget = Gadgets.getGadget(item);
        this.action = action;
        this.hand = hand;
    }


    public GadgetUseEvent(Player player, Action action, Gadget gadget, HandSlot hand) {
        this.player = player;
        this.gadget = gadget;
        this.action = action;
        this.hand = hand;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }

    @Override
    public HandlerList getHandlers() {
        return handler;
    }

    public static HandlerList getHandlerList() {
        return handler;
    }

    public Player getPlayer() {
        return player;
    }

    public Gadget getGadget() {
        return gadget;
    }

    public Action getAction() {
        return action;
    }

    public HandSlot getHand() {
        return hand;
    }

    public boolean hasBlock() {
        return getBlock() != null;
    }

    public static void handle(GadgetUseEvent e) {
        if (e.isCancelled()) {
            return;
        }

        Player player = e.getPlayer();
        Gadget gadget = e.getGadget();

        if (player == null || gadget == null) {
            return;
        }
        if (gadget instanceof Weapon) {
            Weapon weapon = (Weapon) gadget;
            if (e.getAction() == Action.RIGHT_CLICK_AIR) {
                weapon.onActivate(player);
                return;
            }
        }

        if (gadget instanceof BaseGun) {
            LauncherFireEvent event = new LauncherFireEvent(player, (BaseGun) gadget);
            Plugins.callEvent(event);
            LauncherFireEvent.handle(event);
            return;
        }

        if (e.hasBlock() && e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            gadget.onRightClockBlock(player, e.getBlock());
        } else {
            gadget.perform(player);
        }
    }
}
