package io.github.thewebcode.yplugin.gui.builder.gui;

import io.github.thewebcode.yplugin.gui.BaseGui;
import io.github.thewebcode.yplugin.gui.components.InteractionModifier;
import io.github.thewebcode.yplugin.gui.components.exception.GuiException;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.Set;
import java.util.function.Consumer;

public abstract class BaseGuiBuilder<G extends BaseGui, B extends BaseGuiBuilder<G, B>> {
    private Component title = null;
    private int rows = 1;
    private final EnumSet<InteractionModifier> interactionModifiers = EnumSet.noneOf(InteractionModifier.class);

    private Consumer<G> consumer;

    @NotNull
    @Contract("_ -> this")
    public B rows(final int rows) {
        this.rows = rows;
        return (B) this;
    }

    @NotNull
    @Contract("_ -> this")
    public B title(@NotNull final Component title) {
        this.title = title;
        return (B) this;
    }

    @NotNull
    @Contract(" -> this")
    public B disableItemPlace() {
        interactionModifiers.add(InteractionModifier.PREVENT_ITEM_PLACE);
        return (B) this;
    }

    @NotNull
    @Contract(" -> this")
    public B disableItemTake() {
        interactionModifiers.add(InteractionModifier.PREVENT_ITEM_TAKE);
        return (B) this;
    }

    @NotNull
    @Contract(" -> this")
    public B disableItemSwap() {
        interactionModifiers.add(InteractionModifier.PREVENT_ITEM_SWAP);
        return (B) this;
    }

    @NotNull
    @Contract(" -> this")
    public B disableItemDrop() {
        interactionModifiers.add(InteractionModifier.PREVENT_ITEM_DROP);
        return (B) this;
    }

    @NotNull
    @Contract(" -> this")
    public B disableOtherActions() {
        interactionModifiers.add(InteractionModifier.PREVENT_OTHER_ACTIONS);
        return (B) this;
    }

    @NotNull
    @Contract(" -> this")
    public B disableAllInteractions() {
        interactionModifiers.addAll(InteractionModifier.VALUES);
        return (B) this;
    }

    @NotNull
    @Contract(" -> this")
    public B enableItemPlace() {
        interactionModifiers.remove(InteractionModifier.PREVENT_ITEM_PLACE);
        return (B) this;
    }

    @NotNull
    @Contract(" -> this")
    public B enableItemTake() {
        interactionModifiers.remove(InteractionModifier.PREVENT_ITEM_TAKE);
        return (B) this;
    }

    @NotNull
    @Contract(" -> this")
    public B enableItemSwap() {
        interactionModifiers.remove(InteractionModifier.PREVENT_ITEM_SWAP);
        return (B) this;
    }

    @NotNull
    @Contract(" -> this")
    public B enableItemDrop() {
        interactionModifiers.remove(InteractionModifier.PREVENT_ITEM_DROP);
        return (B) this;
    }

    @NotNull
    @Contract(" -> this")
    public B enableOtherActions() {
        interactionModifiers.remove(InteractionModifier.PREVENT_OTHER_ACTIONS);
        return (B) this;
    }

    @NotNull
    @Contract(" -> this")
    public B enableAllInteractions() {
        interactionModifiers.clear();
        return (B) this;
    }

    @NotNull
    @Contract("_ -> this")
    public B apply(@NotNull final Consumer<G> consumer) {
        this.consumer = consumer;
        return (B) this;
    }

    @NotNull
    @Contract(" -> new")
    public abstract G create();

    @NotNull
    protected Component getTitle() {
        if (title == null) {
            throw new GuiException("GUI title is missing!");
        }

        return title;
    }

    protected int getRows() {
        return rows;
    }

    @Nullable
    protected Consumer<G> getConsumer() {
        return consumer;
    }


    @NotNull
    protected Set<InteractionModifier> getModifiers() {
        return interactionModifiers;
    }

}
