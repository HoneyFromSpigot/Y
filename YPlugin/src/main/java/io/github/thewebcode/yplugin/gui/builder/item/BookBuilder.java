package io.github.thewebcode.yplugin.gui.builder.item;

import io.github.thewebcode.yplugin.gui.components.exception.GuiException;
import io.github.thewebcode.yplugin.gui.components.util.Legacy;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

public class BookBuilder extends BaseItemBuilder<BookBuilder>{
    private static final EnumSet<Material> BOOKS = EnumSet.of(Material.WRITABLE_BOOK, Material.WRITTEN_BOOK);

    BookBuilder(@NotNull ItemStack itemStack) {
        super(itemStack);
        if (!BOOKS.contains(itemStack.getType())) {
            throw new GuiException("BookBuilder requires the material to be a WRITABLE_BOOK/WRITTEN_BOOK!");
        }
    }

    @NotNull
    @Contract("_ -> this")
    public BookBuilder author(@Nullable final Component author) {
        final BookMeta bookMeta = (BookMeta) getMeta();

        if (author == null) {
            bookMeta.setAuthor(null);
            setMeta(bookMeta);
            return this;
        }

        bookMeta.setAuthor(Legacy.SERIALIZER.serialize(author));
        setMeta(bookMeta);
        return this;
    }

    @NotNull
    @Contract("_ -> this")
    public BookBuilder generation(@Nullable final BookMeta.Generation generation) {
        final BookMeta bookMeta = (BookMeta) getMeta();

        bookMeta.setGeneration(generation);
        setMeta(bookMeta);
        return this;
    }

    @NotNull
    @Contract("_ -> this")
    public BookBuilder page(@NotNull final Component... pages) {
        return page(Arrays.asList(pages));
    }

    @NotNull
    @Contract("_ -> this")
    public BookBuilder page(@NotNull final List<Component> pages) {
        final BookMeta bookMeta = (BookMeta) getMeta();

        for (final Component page : pages) {
            bookMeta.addPage(Legacy.SERIALIZER.serialize(page));
        }

        setMeta(bookMeta);
        return this;
    }

    @NotNull
    @Contract("_, _ -> this")
    public BookBuilder page(final int page, @NotNull final Component data) {
        final BookMeta bookMeta = (BookMeta) getMeta();

        bookMeta.setPage(page, Legacy.SERIALIZER.serialize(data));
        setMeta(bookMeta);
        return this;
    }

    @NotNull
    @Contract("_ -> this")
    public BookBuilder title(@Nullable Component title) {
        final BookMeta bookMeta = (BookMeta) getMeta();

        if (title == null) {
            bookMeta.setTitle(null);
            setMeta(bookMeta);
            return this;
        }

        bookMeta.setTitle(Legacy.SERIALIZER.serialize(title));
        setMeta(bookMeta);
        return this;
    }

}
