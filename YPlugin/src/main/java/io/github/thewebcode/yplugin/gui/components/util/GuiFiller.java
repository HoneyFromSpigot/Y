package io.github.thewebcode.yplugin.gui.components.util;

import io.github.thewebcode.yplugin.gui.BaseGui;
import io.github.thewebcode.yplugin.gui.GuiItem;
import io.github.thewebcode.yplugin.gui.PaginatedGui;
import io.github.thewebcode.yplugin.gui.components.GuiType;
import io.github.thewebcode.yplugin.gui.components.exception.GuiException;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GuiFiller {
    private final BaseGui gui;

    public GuiFiller(final BaseGui gui) {
        this.gui = gui;
    }

    public void fillTop(@NotNull final GuiItem guiItem) {
        fillTop(Collections.singletonList(guiItem));
    }

    public void fillTop(@NotNull final List<GuiItem> guiItems) {
        final List<GuiItem> items = repeatList(guiItems);
        for (int i = 0; i < 9; i++) {
            if (!gui.getGuiItems().containsKey(i)) gui.setItem(i, items.get(i));
        }
    }

    public void fillBottom(@NotNull final GuiItem guiItem) {
        fillBottom(Collections.singletonList(guiItem));
    }

    public void fillBottom(@NotNull final List<GuiItem> guiItems) {
        final int rows = gui.getRows();
        final List<GuiItem> items = repeatList(guiItems);
        for (int i = 9; i > 0; i--) {
            if (gui.getGuiItems().get((rows * 9) - i) == null) {
                gui.setItem((rows * 9) - i, items.get(i));
            }
        }
    }

    public void fillBorder(@NotNull final GuiItem guiItem) {
        fillBorder(Collections.singletonList(guiItem));
    }

    public void fillBorder(@NotNull final List<GuiItem> guiItems) {
        final int rows = gui.getRows();
        if (rows <= 2) return;

        final List<GuiItem> items = repeatList(guiItems);

        for (int i = 0; i < rows * 9; i++) {
            if ((i <= 8)
                    || (i >= (rows * 9) - 8) && (i <= (rows * 9) - 2)
                    || i % 9 == 0
                    || i % 9 == 8)
                gui.setItem(i, items.get(i));

        }
    }

    public void fillBetweenPoints(final int rowFrom, final int colFrom, final int rowTo, final int colTo, @NotNull final GuiItem guiItem) {
        fillBetweenPoints(rowFrom, colFrom, rowTo, colTo, Collections.singletonList(guiItem));
    }

    public void fillBetweenPoints(final int rowFrom, final int colFrom, final int rowTo, final int colTo, @NotNull final List<GuiItem> guiItems) {
        final int minRow = Math.min(rowFrom, rowTo);
        final int maxRow = Math.max(rowFrom, rowTo);
        final int minCol = Math.min(colFrom, colTo);
        final int maxCol = Math.max(colFrom, colTo);

        final int rows = gui.getRows();
        final List<GuiItem> items = repeatList(guiItems);

        for (int row = 1; row <= rows; row++) {
            for (int col = 1; col <= 9; col++) {
                final int slot = getSlotFromRowCol(row, col);
                if (!((row >= minRow && row <= maxRow) && (col >= minCol && col <= maxCol)))
                    continue;

                gui.setItem(slot, items.get(slot));
            }
        }
    }

    public void fill(@NotNull final GuiItem guiItem) {
        fill(Collections.singletonList(guiItem));
    }

    public void fill(@NotNull final List<GuiItem> guiItems) {
        if (gui instanceof PaginatedGui) {
            throw new GuiException("Full filling a GUI is not supported in a Paginated GUI!");
        }

        final GuiType type = gui.guiType();

        final int fill;
        if (type == GuiType.CHEST) {
            fill = gui.getRows() * type.getLimit();
        } else {
            fill = type.getLimit();
        }

        final List<GuiItem> items = repeatList(guiItems);
        for (int i = 0; i < fill; i++) {
            if (gui.getGuiItems().get(i) == null) gui.setItem(i, items.get(i));
        }
    }

    private List<GuiItem> repeatList(@NotNull final List<GuiItem> guiItems) {
        final List<GuiItem> repeated = new ArrayList<>();
        Collections.nCopies(gui.getRows() * 9, guiItems).forEach(repeated::addAll);
        return repeated;
    }

    private int getSlotFromRowCol(final int row, final int col) {
        return (col + (row - 1) * 9) - 1;
    }
}
