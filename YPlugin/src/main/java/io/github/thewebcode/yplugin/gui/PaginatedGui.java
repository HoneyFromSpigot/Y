package io.github.thewebcode.yplugin.gui;

import io.github.thewebcode.yplugin.gui.components.InteractionModifier;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class PaginatedGui extends BaseGui{
    private final List<GuiItem> pageItems = new ArrayList<>();
    private final Map<Integer, GuiItem> currentPage;

    private int pageSize;
    private int pageNum = 1;

    public PaginatedGui(final int rows, final int pageSize, @NotNull final String title, @NotNull final Set<InteractionModifier> interactionModifiers) {
        super(rows, title, interactionModifiers);
        this.pageSize = pageSize;
        int inventorySize = rows * 9;
        this.currentPage = new LinkedHashMap<>(inventorySize);
    }

    @Deprecated
    public PaginatedGui(final int rows, final int pageSize, @NotNull final String title) {
        super(rows, title);
        this.pageSize = pageSize;
        int inventorySize = rows * 9;
        this.currentPage = new LinkedHashMap<>(inventorySize);
    }

    @Deprecated
    public PaginatedGui(final int rows, @NotNull final String title) {
        this(rows, 0, title);
    }

    @Deprecated
    public PaginatedGui(@NotNull final String title) {
        this(2, title);
    }

    public BaseGui setPageSize(final int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public void addItem(@NotNull final GuiItem item) {
        pageItems.add(item);
    }

    @Override
    public void addItem(@NotNull final GuiItem... items) {
        pageItems.addAll(Arrays.asList(items));
    }

    @Override
    public void update() {
        getInventory().clear();
        populateGui();

        updatePage();
    }

    public void updatePageItem(final int slot, @NotNull final ItemStack itemStack) {
        if (!currentPage.containsKey(slot)) return;
        final GuiItem guiItem = currentPage.get(slot);
        guiItem.setItemStack(itemStack);
        getInventory().setItem(slot, guiItem.getItemStack());
    }

    public void updatePageItem(final int row, final int col, @NotNull final ItemStack itemStack) {
        updateItem(getSlotFromRowCol(row, col), itemStack);
    }

    public void updatePageItem(final int slot, @NotNull final GuiItem item) {
        if (!currentPage.containsKey(slot)) return;
        final GuiItem oldItem = currentPage.get(slot);
        final int index = pageItems.indexOf(currentPage.get(slot));
        currentPage.put(slot, item);
        pageItems.set(index, item);
        getInventory().setItem(slot, item.getItemStack());
    }

    public void updatePageItem(final int row, final int col, @NotNull final GuiItem item) {
        updateItem(getSlotFromRowCol(row, col), item);
    }

    public void removePageItem(@NotNull final GuiItem item) {
        pageItems.remove(item);
        updatePage();
    }

    public void removePageItem(@NotNull final ItemStack item) {
        final Optional<GuiItem> guiItem = pageItems.stream().filter(it -> it.getItemStack().equals(item)).findFirst();
        guiItem.ifPresent(this::removePageItem);
    }

    @Override
    public void open(@NotNull final HumanEntity player) {
        open(player, 1);
    }

    public void open(@NotNull final HumanEntity player, final int openPage) {
        if (player.isSleeping()) return;
        if (openPage <= getPagesNum() || openPage > 0) pageNum = openPage;

        getInventory().clear();
        currentPage.clear();

        populateGui();

        if (pageSize == 0) pageSize = calculatePageSize();

        populatePage();

        player.openInventory(getInventory());
    }

    @Override
    @NotNull
    public BaseGui updateTitle(@NotNull final String title) {
        setUpdating(true);

        final List<HumanEntity> viewers = new ArrayList<>(getInventory().getViewers());

        setInventory(Bukkit.createInventory(this, getInventory().getSize(), title));

        for (final HumanEntity player : viewers) {
            open(player, getPageNum());
        }

        setUpdating(false);

        return this;
    }

    @NotNull
    public Map<@NotNull Integer, @NotNull GuiItem> getCurrentPageItems() {
        return Collections.unmodifiableMap(currentPage);
    }

    @NotNull
    public List<@NotNull GuiItem> getPageItems() {
        return Collections.unmodifiableList(pageItems);
    }


    public int getCurrentPageNum() {
        return pageNum;
    }

    public int getNextPageNum() {
        if (pageNum + 1 > getPagesNum()) return pageNum;
        return pageNum + 1;
    }

    public int getPrevPageNum() {
        if (pageNum - 1 == 0) return pageNum;
        return pageNum - 1;
    }

    public boolean next() {
        if (pageNum + 1 > getPagesNum()) return false;

        pageNum++;
        updatePage();
        return true;
    }

    public boolean previous() {
        if (pageNum - 1 == 0) return false;

        pageNum--;
        updatePage();
        return true;
    }

    GuiItem getPageItem(final int slot) {
        return currentPage.get(slot);
    }

    private List<GuiItem> getPageNum(final int givenPage) {
        final int page = givenPage - 1;

        final List<GuiItem> guiPage = new ArrayList<>();

        int max = ((page * pageSize) + pageSize);
        if (max > pageItems.size()) max = pageItems.size();

        for (int i = page * pageSize; i < max; i++) {
            guiPage.add(pageItems.get(i));
        }

        return guiPage;
    }

    public int getPagesNum() {
        return (int) Math.ceil((double) pageItems.size() / pageSize);
    }

    private void populatePage() {
        for (final GuiItem guiItem : getPageNum(pageNum)) {
            for (int slot = 0; slot < getRows() * 9; slot++) {
                if (getGuiItem(slot) != null || getInventory().getItem(slot) != null) continue;
                currentPage.put(slot, guiItem);
                getInventory().setItem(slot, guiItem.getItemStack());
                break;
            }
        }
    }

    Map<Integer, GuiItem> getMutableCurrentPageItems() {
        return currentPage;
    }

    void clearPage() {
        for (Map.Entry<Integer, GuiItem> entry : currentPage.entrySet()) {
            getInventory().setItem(entry.getKey(), null);
        }
    }

    public void clearPageItems(final boolean update) {
        pageItems.clear();
        if (update) update();
    }

    public void clearPageItems() {
        clearPageItems(false);
    }


    int getPageSize() {
        return pageSize;
    }

    int getPageNum() {
        return pageNum;
    }

    void setPageNum(final int pageNum) {
        this.pageNum = pageNum;
    }

    void updatePage() {
        clearPage();
        populatePage();
    }

    int calculatePageSize() {
        int counter = 0;

        for (int slot = 0; slot < getRows() * 9; slot++) {
            if (getInventory().getItem(slot) == null) counter++;
        }

        return counter;
    }
}
