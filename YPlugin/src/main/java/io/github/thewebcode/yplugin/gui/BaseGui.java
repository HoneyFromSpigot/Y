package io.github.thewebcode.yplugin.gui;

import io.github.thewebcode.yplugin.gui.components.GuiAction;
import io.github.thewebcode.yplugin.gui.components.GuiType;
import io.github.thewebcode.yplugin.gui.components.InteractionModifier;
import io.github.thewebcode.yplugin.gui.components.exception.GuiException;
import io.github.thewebcode.yplugin.gui.components.util.GuiFiller;
import io.github.thewebcode.yplugin.gui.components.util.Legacy;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class BaseGui implements InventoryHolder {
    private static final Plugin plugin = JavaPlugin.getProvidingPlugin(BaseGui.class);

    static {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new GuiListener(), plugin);
        pluginManager.registerEvents(new InteractionModifiedListener(), plugin);
    }

    private final Map<Integer, GuiAction<InventoryClickEvent>> slotActions;
    private final Set<InteractionModifier> interactionModifiers;
    private final GuiFiller filler = new GuiFiller(this);
    private final Map<Integer, GuiItem> guiItems;
    private GuiType guiType = GuiType.CHEST;
    private int rows = 1;
    private String title;
    private Inventory inventory;
    private boolean updating;
    private GuiAction<InventoryClickEvent> defaultClickAction;
    private GuiAction<InventoryClickEvent> defaultTopClickAction;
    private GuiAction<InventoryClickEvent> playerInventoryAction;
    private GuiAction<InventoryDragEvent> dragAction;
    private GuiAction<InventoryCloseEvent> closeGuiAction;
    private GuiAction<InventoryOpenEvent> openGuiAction;
    private GuiAction<InventoryClickEvent> outsideClickAction;
    private boolean runCloseAction = true;
    private boolean runOpenAction = true;

    public BaseGui(int rows, String title, Set<InteractionModifier> interactionModifiers) {
        int finalRows = rows;
        if (!(rows >= 1 && rows <= 6)) finalRows = 1;
        this.rows = finalRows;
        this.interactionModifiers = safeCopyOf(interactionModifiers);
        this.title = title;
        int inventorySize = this.rows * 9;
        this.inventory = Bukkit.createInventory(this, inventorySize, title);
        this.slotActions = new LinkedHashMap<>(inventorySize);
        this.guiItems = new LinkedHashMap<>(inventorySize);
    }

    public BaseGui(@NotNull final GuiType guiType, @NotNull final String title, @NotNull final Set<InteractionModifier> interactionModifiers) {
        this.guiType = guiType;
        this.interactionModifiers = safeCopyOf(interactionModifiers);
        this.title = title;
        int inventorySize = guiType.getLimit();
        this.inventory = Bukkit.createInventory(this, guiType.getInventoryType(), title);
        this.slotActions = new LinkedHashMap<>(inventorySize);
        this.guiItems = new LinkedHashMap<>(inventorySize);
    }

    @NotNull
    private EnumSet<InteractionModifier> safeCopyOf(@NotNull final Set<InteractionModifier> set) {
        if (set.isEmpty()) return EnumSet.noneOf(InteractionModifier.class);
        else return EnumSet.copyOf(set);
    }

    @Deprecated
    public BaseGui(final int rows, @NotNull final String title) {
        int finalRows = rows;
        if (!(rows >= 1 && rows <= 6)) finalRows = 1;
        this.rows = finalRows;
        this.interactionModifiers = EnumSet.noneOf(InteractionModifier.class);
        this.title = title;

        inventory = Bukkit.createInventory(this, this.rows * 9, title);
        slotActions = new LinkedHashMap<>();
        guiItems = new LinkedHashMap<>();
    }

    @Deprecated
    public BaseGui(@NotNull final GuiType guiType, @NotNull final String title) {
        this.guiType = guiType;
        this.interactionModifiers = EnumSet.noneOf(InteractionModifier.class);
        this.title = title;

        inventory = Bukkit.createInventory(this, this.guiType.getInventoryType(), title);
        slotActions = new LinkedHashMap<>();
        guiItems = new LinkedHashMap<>();
    }

    @NotNull
    @Deprecated
    public String getTitle() {
        return title;
    }

    @NotNull
    public Component title() {
        return Legacy.SERIALIZER.deserialize(title);
    }

    public void setItem(final int slot, @NotNull final GuiItem guiItem) {
        validateSlot(slot);
        guiItems.put(slot, guiItem);
    }

    public void removeItem(@NotNull final GuiItem item) {
        final Optional<Map.Entry<Integer, GuiItem>> entry = guiItems.entrySet()
                .stream()
                .filter(it -> it.getValue().equals(item))
                .findFirst();

        entry.ifPresent(it -> {
            guiItems.remove(it.getKey());
            inventory.remove(it.getValue().getItemStack());
        });
    }

    public void removeItem(@NotNull final ItemStack item) {
        final Optional<Map.Entry<Integer, GuiItem>> entry = guiItems.entrySet()
                .stream()
                .filter(it -> it.getValue().getItemStack().equals(item))
                .findFirst();

        entry.ifPresent(it -> {
            guiItems.remove(it.getKey());
            inventory.remove(item);
        });
    }

    public void removeItem(final int slot) {
        validateSlot(slot);
        guiItems.remove(slot);
        inventory.setItem(slot, null);
    }

    public void removeItem(final int row, final int col) {
        removeItem(getSlotFromRowCol(row, col));
    }

    public void setItem(@NotNull final List<Integer> slots, @NotNull final GuiItem guiItem) {
        for (final int slot : slots) {
            setItem(slot, guiItem);
        }
    }

    public void setItem(final int row, final int col, @NotNull final GuiItem guiItem) {
        setItem(getSlotFromRowCol(row, col), guiItem);
    }

    public void addItem(@NotNull final GuiItem... items) {
        this.addItem(false, items);
    }

    public void addItem(final boolean expandIfFull, @NotNull final GuiItem... items) {
        final List<GuiItem> notAddedItems = new ArrayList<>();

        for (final GuiItem guiItem : items) {
            for (int slot = 0; slot < rows * 9; slot++) {
                if (guiItems.get(slot) != null) {
                    if (slot == rows * 9 - 1) {
                        notAddedItems.add(guiItem);
                    }
                    continue;
                }

                guiItems.put(slot, guiItem);
                break;
            }
        }

        if (!expandIfFull || this.rows >= 6 ||
                notAddedItems.isEmpty() ||
                (this.guiType != null && this.guiType != GuiType.CHEST)) {
            return;
        }

        this.rows++;
        this.inventory = Bukkit.createInventory(this, this.rows * 9, this.title);
        this.update();
        this.addItem(true, notAddedItems.toArray(new GuiItem[0]));
    }

    public void setDefaultClickAction(@Nullable final GuiAction<@NotNull InventoryClickEvent> defaultClickAction) {
        this.defaultClickAction = defaultClickAction;
    }

    public void setDefaultTopClickAction(@Nullable final GuiAction<@NotNull InventoryClickEvent> defaultTopClickAction) {
        this.defaultTopClickAction = defaultTopClickAction;
    }

    public void setPlayerInventoryAction(@Nullable final GuiAction<@NotNull InventoryClickEvent> playerInventoryAction) {
        this.playerInventoryAction = playerInventoryAction;
    }

    public void setOutsideClickAction(@Nullable final GuiAction<@NotNull InventoryClickEvent> outsideClickAction) {
        this.outsideClickAction = outsideClickAction;
    }

    public void setDragAction(@Nullable final GuiAction<@NotNull InventoryDragEvent> dragAction) {
        this.dragAction = dragAction;
    }

    public void setCloseGuiAction(@Nullable final GuiAction<@NotNull InventoryCloseEvent> closeGuiAction) {
        this.closeGuiAction = closeGuiAction;
    }

    public void setOpenGuiAction(@Nullable final GuiAction<@NotNull InventoryOpenEvent> openGuiAction) {
        this.openGuiAction = openGuiAction;
    }

    public void addSlotAction(final int slot, @Nullable final GuiAction<@NotNull InventoryClickEvent> slotAction) {
        validateSlot(slot);
        slotActions.put(slot, slotAction);
    }

    public void addSlotAction(final int row, final int col, @Nullable final GuiAction<@NotNull InventoryClickEvent> slotAction) {
        addSlotAction(getSlotFromRowCol(row, col), slotAction);
    }

    @Nullable
    public GuiItem getGuiItem(final int slot) {
        return guiItems.get(slot);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isUpdating() {
        return updating;
    }

    public void setUpdating(final boolean updating) {
        this.updating = updating;
    }

    public void open(@NotNull final HumanEntity player) {
        if (player.isSleeping()) return;

        inventory.clear();
        populateGui();
        player.openInventory(inventory);
    }

    public void close(@NotNull final HumanEntity player) {
        close(player, true);
    }

    public void close(@NotNull final HumanEntity player, final boolean runCloseAction) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            this.runCloseAction = runCloseAction;
            player.closeInventory();
            this.runCloseAction = true;
        }, 2L);
    }

    public void update() {
        inventory.clear();
        populateGui();
        for (HumanEntity viewer : new ArrayList<>(inventory.getViewers())) ((Player) viewer).updateInventory();
    }

    @Contract("_ -> this")
    @NotNull
    public BaseGui updateTitle(@NotNull final String title) {
        updating = true;

        final List<HumanEntity> viewers = new ArrayList<>(inventory.getViewers());

        inventory = Bukkit.createInventory(this, inventory.getSize(), title);

        for (final HumanEntity player : viewers) {
            open(player);
        }

        updating = false;
        this.title = title;
        return this;
    }

    public void updateItem(final int slot, @NotNull final ItemStack itemStack) {
        final GuiItem guiItem = guiItems.get(slot);

        if (guiItem == null) {
            updateItem(slot, new GuiItem(itemStack));
            return;
        }

        guiItem.setItemStack(itemStack);
        updateItem(slot, guiItem);
    }

    public void updateItem(final int row, final int col, @NotNull final ItemStack itemStack) {
        updateItem(getSlotFromRowCol(row, col), itemStack);
    }

    public void updateItem(final int slot, @NotNull final GuiItem item) {
        guiItems.put(slot, item);
        inventory.setItem(slot, item.getItemStack());
    }

    public void updateItem(final int row, final int col, @NotNull final GuiItem item) {
        updateItem(getSlotFromRowCol(row, col), item);
    }

    @NotNull
    @Contract(" -> this")
    public BaseGui disableItemPlace() {
        interactionModifiers.add(InteractionModifier.PREVENT_ITEM_PLACE);
        return this;
    }

    @NotNull
    @Contract(" -> this")
    public BaseGui disableItemTake() {
        interactionModifiers.add(InteractionModifier.PREVENT_ITEM_TAKE);
        return this;
    }

    @NotNull
    @Contract(" -> this")
    public BaseGui disableItemSwap() {
        interactionModifiers.add(InteractionModifier.PREVENT_ITEM_SWAP);
        return this;
    }

    @NotNull
    @Contract(" -> this")
    public BaseGui disableItemDrop() {
        interactionModifiers.add(InteractionModifier.PREVENT_ITEM_DROP);
        return this;
    }

    @NotNull
    @Contract(" -> this")
    public BaseGui disableOtherActions() {
        interactionModifiers.add(InteractionModifier.PREVENT_OTHER_ACTIONS);
        return this;
    }

    @NotNull
    @Contract(" -> this")
    public BaseGui disableAllInteractions() {
        interactionModifiers.addAll(InteractionModifier.VALUES);
        return this;
    }

    @NotNull
    @Contract(" -> this")
    public BaseGui enableItemPlace() {
        interactionModifiers.remove(InteractionModifier.PREVENT_ITEM_PLACE);
        return this;
    }

    @NotNull
    @Contract(" -> this")
    public BaseGui enableItemTake() {
        interactionModifiers.remove(InteractionModifier.PREVENT_ITEM_TAKE);
        return this;
    }

    @NotNull
    @Contract(" -> this")
    public BaseGui enableItemSwap() {
        interactionModifiers.remove(InteractionModifier.PREVENT_ITEM_SWAP);
        return this;
    }

    @NotNull
    @Contract(" -> this")
    public BaseGui enableItemDrop() {
        interactionModifiers.remove(InteractionModifier.PREVENT_ITEM_DROP);
        return this;
    }

    @NotNull
    @Contract(" -> this")
    public BaseGui enableOtherActions() {
        interactionModifiers.remove(InteractionModifier.PREVENT_OTHER_ACTIONS);
        return this;
    }

    @NotNull
    @Contract(" -> this")
    public BaseGui enableAllInteractions() {
        interactionModifiers.clear();
        return this;
    }

    public boolean canPlaceItems() {
        return !interactionModifiers.contains(InteractionModifier.PREVENT_ITEM_PLACE);
    }

    public boolean canTakeItems() {
        return !interactionModifiers.contains(InteractionModifier.PREVENT_ITEM_TAKE);
    }

    public boolean canSwapItems() {
        return !interactionModifiers.contains(InteractionModifier.PREVENT_ITEM_SWAP);
    }

    public boolean canDropItems() {
        return !interactionModifiers.contains(InteractionModifier.PREVENT_ITEM_DROP);
    }

    public boolean allowsOtherActions() {
        return !interactionModifiers.contains(InteractionModifier.PREVENT_OTHER_ACTIONS);
    }

    @NotNull
    public GuiFiller getFiller() {
        return filler;
    }

    @NotNull
    public Map<@NotNull Integer, @NotNull GuiItem> getGuiItems() {
        return guiItems;
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public int getRows() {
        return rows;
    }

    @NotNull
    public GuiType guiType() {
        return guiType;
    }

    @Nullable
    GuiAction<InventoryClickEvent> getDefaultClickAction() {
        return defaultClickAction;
    }

    @Nullable
    GuiAction<InventoryClickEvent> getDefaultTopClickAction() {
        return defaultTopClickAction;
    }

    @Nullable
    GuiAction<InventoryClickEvent> getPlayerInventoryAction() {
        return playerInventoryAction;
    }

    @Nullable
    GuiAction<InventoryDragEvent> getDragAction() {
        return dragAction;
    }

    @Nullable
    GuiAction<InventoryCloseEvent> getCloseGuiAction() {
        return closeGuiAction;
    }

    @Nullable
    GuiAction<InventoryOpenEvent> getOpenGuiAction() {
        return openGuiAction;
    }

    @Nullable
    GuiAction<InventoryClickEvent> getOutsideClickAction() {
        return outsideClickAction;
    }

    @Nullable
    GuiAction<InventoryClickEvent> getSlotAction(final int slot) {
        return slotActions.get(slot);
    }

    void populateGui() {
        for (final Map.Entry<Integer, GuiItem> entry : guiItems.entrySet()) {
            inventory.setItem(entry.getKey(), entry.getValue().getItemStack());
        }
    }

    boolean shouldRunCloseAction() {
        return runCloseAction;
    }

    boolean shouldRunOpenAction() {
        return runOpenAction;
    }

    int getSlotFromRowCol(final int row, final int col) {
        return (col + (row - 1) * 9) - 1;
    }

    public void setInventory(@NotNull final Inventory inventory) {
        this.inventory = inventory;
    }

    private void validateSlot(final int slot) {
        final int limit = guiType.getLimit();

        if (guiType == GuiType.CHEST) {
            if (slot < 0 || slot >= rows * limit) throwInvalidSlot(slot);
            return;
        }

        if (slot < 0 || slot > limit) throwInvalidSlot(slot);
    }

    private void throwInvalidSlot(final int slot) {
        if (guiType == GuiType.CHEST) {
            throw new GuiException("Slot " + slot + " is not valid for the gui type - " + guiType.name() + " and rows - " + rows + "!");
        }

        throw new GuiException("Slot " + slot + " is not valid for the gui type - " + guiType.name() + "!");
    }
}
