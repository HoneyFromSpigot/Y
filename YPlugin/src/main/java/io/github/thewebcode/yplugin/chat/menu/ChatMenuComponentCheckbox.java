package io.github.thewebcode.yplugin.chat.menu;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class ChatMenuComponentCheckbox extends ChatMenuComponent {

    public static final String DEFAULT_FORMAT = " [%s] ";
    public static final String YES = "✔";
    public static final String NO = "✖";
    public static final String EMPTY = " ";

    private boolean checked;
    private String format = DEFAULT_FORMAT;
    private String stringChecked = YES;
    private String stringUnchecked = NO;

    private ValueListener<Boolean> valueListener;

    public ChatMenuComponentCheckbox() {
        this(false);
    }

    public ChatMenuComponentCheckbox(boolean checked) {
        this.checked = checked;
        updateText();
    }

    public ChatMenuComponentCheckbox withFormat(@Nonnull String format) {
        this.format = format;
        updateText();
        return this;
    }

    public ChatMenuComponentCheckbox withCheckedString(@Nonnull String stringChecked) {
        this.stringChecked = stringChecked;
        updateText();
        return this;
    }

    public ChatMenuComponentCheckbox withUncheckedString(@Nonnull String stringUnchecked) {
        this.stringUnchecked = stringUnchecked;
        updateText();
        return this;
    }

    public boolean isChecked() {
        return checked;
    }

    public ChatMenuComponentCheckbox setChecked(boolean checked) {
        this.checked = checked;
        updateText();
        return this;
    }

    public ChatMenuComponentCheckbox onChange(final ValueListener<Boolean> listener) {
        this.valueListener = listener;
        return this;
    }

    @Override
    public String render() {
        String formatted = this.format;

        formatted = String.format(formatted, isChecked() ? stringChecked : stringUnchecked);

        return formatted;
    }

    public TextComponent build() {
        return this.component;
    }

    public ChatMenuComponent appendTo(final LineBuilder builder) {
        builder.append(new ChatMenuListener() {
            @Override
            public void onClick(Player player) {
                boolean wasChecked = isChecked();
                setChecked(!wasChecked);
                if (valueListener != null) {
                    valueListener.onChange(player, wasChecked, isChecked());
                }
                if (builder.getContainer() != null) {
                    builder.getContainer().refreshContent();
                }
            }
        }, build());
        return this;
    }
}
