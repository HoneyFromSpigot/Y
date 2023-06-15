package io.github.thewebcode.yplugin.game.item;

import io.github.thewebcode.yplugin.YPlugin;
import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.game.gadget.GadgetProperties;
import io.github.thewebcode.yplugin.game.gadget.ItemGadget;
import io.github.thewebcode.yplugin.inventory.HandSlot;
import io.github.thewebcode.yplugin.item.ItemBuilder;
import io.github.thewebcode.yplugin.player.Players;
import io.github.thewebcode.yplugin.time.TimeHandler;
import io.github.thewebcode.yplugin.time.TimeType;
import io.github.thewebcode.yplugin.world.Worlds;
import io.github.thewebcode.yplugin.yml.Comment;
import io.github.thewebcode.yplugin.yml.Path;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;

public abstract class ThrowableItem extends ItemGadget {

    private Properties properties = new Properties();

    public ThrowableItem(ItemStack item) {
        super(item);
    }

    public ThrowableItem(ItemStack item, int delay) {
        super(item);
        properties().delay(delay);
    }

    public ThrowableItem(ItemBuilder builder) {
        super(builder);
    }

    public ThrowableItem(ItemBuilder builder, int delay) {
        super(builder);
        properties().delay(delay);
    }

    @Override
    public void perform(Player holder) {
        ItemStack gadgetItem = getItem();
        if (properties().takeItem()) {
            YPlugin.getInstance().getThreadManager().runTaskOneTickLater(() -> {
                Players.removeFromHand(holder, 1, HandSlot.MAIN_HAND);
            });
        }

        Location eyeLoc = holder.getEyeLocation();

        final Item thrownItem = Worlds.dropItem(eyeLoc, gadgetItem);

        if (thrownItem == null) {
            return;
        }
        if (!properties().canPickup()) {
            thrownItem.setPickupDelay(Integer.MAX_VALUE);
        }

        thrownItem.setVelocity(eyeLoc.getDirection().multiply(properties().force()));

        Action action = properties().action();
        switch (action) {
            case DELAY:
                YPlugin.getInstance().getThreadManager().runTaskLater(new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (thrownItem == null) {
                            cancel();
                            return;
                        }
                        handle(holder, thrownItem);

                        if (properties().action() == Action.CANCEL) {
                            Chat.actionMessage(holder, properties().cancelMessage());
                            properties().action(Action.DELAY);
                            return;
                        }
                        if (properties().removeItem()) {
                            thrownItem.remove();
                        }
                    }
                }, TimeHandler.getTimeInTicks(properties().delay(), properties().delayType()));
                break;
            case REPEAT_TICK:
                long reTicks;
                if (properties().isTicks()) {
                    reTicks = properties().delay();
                } else {
                    reTicks = TimeHandler.getTimeInTicks(properties().delay(), properties().delayType());
                }

                YPlugin.getInstance().getThreadManager().registerSyncRepeatTask("Gadget[" + thrownItem.getUniqueId().toString() + "-TICK]", new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (thrownItem == null) {
                            cancel();
                            return;
                        }
                        handle(holder, thrownItem);
                        if (!thrownItem.isValid()) {
                            cancel();
                        }
                    }
                }, reTicks, reTicks);
                break;
            case EXECUTE:
                long exTicks = properties().isTicks() ? properties().delay() : 50l;
                YPlugin.getInstance().getThreadManager().runTaskLater(() -> {
                    handle(holder, thrownItem);

                    if (properties().action() == Action.CANCEL) {
                        Chat.actionMessage(holder, properties().cancelMessage());
                        properties().action(Action.EXECUTE);
                    }

                    if (properties().removeItem()) {
                        if (thrownItem.isValid()) {
                            thrownItem.remove();
                        }
                    }
                }, exTicks);
                break;
            case CANCEL:
                Chat.actionMessage(holder, properties().cancelMessage());
                break;

        }

    }

    public abstract void handle(Player holder, Item thrownItem);

    @Override
    public Properties properties() {
        return properties;
    }

    public enum Action {
        DELAY,
        REPEAT_TICK,
        EXECUTE,
        CANCEL
    }

    public class Properties extends GadgetProperties {
        @Path("force")
        private double force;

        @Path("delay")
        private int delay = 40;

        @Path("delay-in-ticks")
        private boolean ticks = false;

        @Path("time-type")
        private String timeTypeString = TimeType.SECOND.name();

        @Path("pickupable")
        private boolean pickupable = false;

        @Path("remove-item")
        private boolean removeItem = true;

        @Path("take-item")
        @Comment("Whether or not the item is taken once thrown (on interact / right click)")
        private boolean takeItem = true;

        @Path("action")
        @Comment("What action to perform after the item has been thrown")
        private String action = Action.EXECUTE.name();

        @Path("cancel-message")
        private String cancelMessage = "";


        public Properties(File file) {
            super(file);
        }

        public Properties() {
            super();
        }


        public int delay() {
            return delay;
        }

        public Properties delay(int delay) {
            this.delay = delay;
            return this;
        }

        public Properties delayType(TimeType type) {
            this.timeTypeString = type.name();
            return this;
        }

        public Properties useTicks(boolean val) {
            this.ticks = val;
            return this;
        }

        public Properties removeItem(boolean val) {
            this.removeItem = val;
            return this;
        }

        public TimeType delayType() {
            return TimeType.valueOf(timeTypeString);
        }

        public double force() {
            return force;
        }

        public Properties force(double force) {
            this.force = force;
            return this;
        }

        public boolean canPickup() {
            return pickupable;
        }

        public Properties canPickup(boolean value) {
            this.pickupable = value;
            return this;
        }

        public Properties action(Action action) {
            this.action = action.name();
            return this;
        }

        public Properties cancel(String message) {
            this.cancelMessage = message;
            this.action = Action.CANCEL.name();
            return this;
        }

        public Properties cancelMessage(String message) {
            this.cancelMessage = message;
            return this;
        }

        public Action action() {
            return Action.valueOf(action);
        }

        public String cancelMessage() {
            return cancelMessage;
        }

        public boolean removeItem() {
            return removeItem;
        }

        public boolean isTicks() {
            return ticks;
        }

        public boolean takeItem() {
            return takeItem;
        }

        public Properties takeItem(boolean value) {
            this.takeItem = value;
            return this;
        }
    }

}
