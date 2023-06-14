package io.github.thewebcode.yplugin.gui;

import io.github.thewebcode.yplugin.gui.components.InteractionModifier;
import io.github.thewebcode.yplugin.gui.components.ScrollType;
import org.bukkit.entity.HumanEntity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ScrollingGui extends PaginatedGui{
    private final ScrollType scrollType;
    private int scrollSize = 0;

    public ScrollingGui(final int rows, final int pageSize, @NotNull final String title, @NotNull final ScrollType scrollType, @NotNull final Set<InteractionModifier> interactionModifiers) {
        super(rows, pageSize, title, interactionModifiers);

        this.scrollType = scrollType;
    }

    @Deprecated
    public ScrollingGui(final int rows, final int pageSize, @NotNull final String title, @NotNull final ScrollType scrollType) {
        super(rows, pageSize, title);
        this.scrollType = scrollType;
    }

    @Deprecated
    public ScrollingGui(final int rows, final int pageSize, @NotNull final String title) {
        this(rows, pageSize, title, ScrollType.VERTICAL);
    }

    @Deprecated
    public ScrollingGui(final int rows, @NotNull final String title) {
        this(rows, 0, title, ScrollType.VERTICAL);
    }

    @Deprecated
    public ScrollingGui(final int rows, @NotNull final String title, @NotNull final ScrollType scrollType) {
        this(rows, 0, title, scrollType);
    }

    @Deprecated
    public ScrollingGui(@NotNull final String title) {
        this(2, title);
    }

    @Deprecated
    public ScrollingGui(@NotNull final String title, @NotNull final ScrollType scrollType) {
        this(2, title, scrollType);
    }

    @Override
    public boolean next() {
        if (getPageNum() * scrollSize + getPageSize() > getPageItems().size() + scrollSize) return false;

        setPageNum(getPageNum() + 1);
        updatePage();
        return true;
    }

    @Override
    public boolean previous() {
        if (getPageNum() - 1 == 0) return false;

        setPageNum(getPageNum() - 1);
        updatePage();
        return true;
    }

    @Override
    public void open(@NotNull final HumanEntity player) {
        open(player, 1);
    }

    @Override
    public void open(@NotNull final HumanEntity player, final int openPage) {
        if (player.isSleeping()) return;
        getInventory().clear();
        getMutableCurrentPageItems().clear();

        populateGui();

        if (getPageSize() == 0) setPageSize(calculatePageSize());
        if (scrollSize == 0) scrollSize = calculateScrollSize();
        if (openPage > 0 && (openPage * scrollSize + getPageSize() <= getPageItems().size() + scrollSize)) {
            setPageNum(openPage);
        }

        populatePage();

        player.openInventory(getInventory());
    }

    @Override
    void updatePage() {
        clearPage();
        populatePage();
    }

    private void populatePage() {
        for (final GuiItem guiItem : getPage(getPageNum())) {
            if (scrollType == ScrollType.HORIZONTAL) {
                putItemHorizontally(guiItem);
                continue;
            }

            putItemVertically(guiItem);
        }
    }

    private int calculateScrollSize() {
        int counter = 0;

        if (scrollType == ScrollType.VERTICAL) {
            boolean foundCol = false;

            for (int row = 1; row <= getRows(); row++) {
                for (int col = 1; col <= 9; col++) {
                    final int slot = getSlotFromRowCol(row, col);
                    if (getInventory().getItem(slot) == null) {
                        if (!foundCol) foundCol = true;
                        counter++;
                    }
                }

                if (foundCol) return counter;
            }

            return counter;
        }

        boolean foundRow = false;

        for (int col = 1; col <= 9; col++) {
            for (int row = 1; row <= getRows(); row++) {
                final int slot = getSlotFromRowCol(row, col);
                if (getInventory().getItem(slot) == null) {
                    if (!foundRow) foundRow = true;
                    counter++;
                }
            }

            if (foundRow) return counter;
        }

        return counter;
    }

    private void putItemVertically(final GuiItem guiItem) {
        for (int slot = 0; slot < getRows() * 9; slot++) {
            if (getGuiItem(slot) != null || getInventory().getItem(slot) != null) continue;
            getMutableCurrentPageItems().put(slot, guiItem);
            getInventory().setItem(slot, guiItem.getItemStack());
            break;
        }
    }

    private void putItemHorizontally(final GuiItem guiItem) {
        for (int col = 1; col < 10; col++) {
            for (int row = 1; row <= getRows(); row++) {
                final int slot = getSlotFromRowCol(row, col);
                if (getGuiItem(slot) != null || getInventory().getItem(slot) != null) continue;
                getMutableCurrentPageItems().put(slot, guiItem);
                getInventory().setItem(slot, guiItem.getItemStack());
                return;
            }
        }
    }

    private List<GuiItem> getPage(final int givenPage) {
        final int page = givenPage - 1;
        final int pageItemsSize = getPageItems().size();

        final List<GuiItem> guiPage = new ArrayList<>();

        int max = page * scrollSize + getPageSize();
        if (max > pageItemsSize) max = pageItemsSize;

        for (int i = page * scrollSize; i < max; i++) {
            guiPage.add(getPageItems().get(i));
        }

        return guiPage;
    }
}
