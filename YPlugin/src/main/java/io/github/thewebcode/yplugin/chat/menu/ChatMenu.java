package io.github.thewebcode.yplugin.chat.menu;

import io.github.thewebcode.yplugin.YPlugin;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import javax.annotation.Nonnegative;
import java.util.ArrayList;
import java.util.List;

public class ChatMenu {

    private List<BaseComponent> lines = new ArrayList<>();
    private List<String> listenerKeys = new ArrayList<>();

    private List<LineCallback> callbackLines = new ArrayList<>();

    private List<HumanEntity> viewers = new ArrayList<>();

    public ChatMenu() {
    }

    public ChatMenu withLine(LineBuilder... builders) {
        for (LineBuilder builder : builders) {
            listenerKeys.addAll(builder.listenerKeys);
            builder.withContainer(this);
            this.lines.add(builder.build());
        }
        return this;
    }

    public ChatMenu withLine(String text, ChatMenuListener onClick) {
        return withLine(new LineBuilder().append(onClick, new TextComponent(text)));
    }

    public ChatMenu withLine(@Nonnegative int index, LineBuilder builder) {
        listenerKeys.addAll(builder.listenerKeys);
        builder.withContainer(this);
        if (lines.size() <= index) {
            this.lines.add(index, builder.build());
        } else {
            this.lines.set(index, builder.build());
        }
        return this;
    }

    public ChatMenu withLine(String... lines) {
        for (String line : lines) {
            this.lines.add(new TextComponent(line));
        }
        return this;
    }

    public ChatMenu withLine(TextComponent... textComponents) {
        for (TextComponent comp : textComponents) {
            this.lines.add(comp);
        }
        return this;
    }

    public ChatMenu withLine(LineCallback callback) {
        callbackLines.add(callback);
        return this;
    }

    public ChatMenu show(HumanEntity... viewers) {
        BaseComponent[] components = build();
        for (HumanEntity viewer : viewers) {
            if (viewer instanceof Player) {
                for (BaseComponent component : components) {
                    ((Player) viewer).spigot().sendMessage(component);
                }
                if (!this.viewers.contains(viewer)) {
                    this.viewers.add(viewer);
                }
            }
        }
        return this;
    }

    public ChatMenu refreshContent() {
        for (LineCallback callback : callbackLines) {
            int index = callback.getIndex();
            LineBuilder builder = callback.getLine();

            withLine(index, builder);
        }

        for (HumanEntity viewer : this.viewers) {
            show(viewer);
        }
        return this;
    }

    public BaseComponent[] build() {
        return this.lines.toArray(new BaseComponent[this.lines.size()]);
    }

    public void dispose() {
        for (String key : this.listenerKeys) {
            YPlugin.getInstance().getChatMenuListener().unregisterListener(key);
        }
    }
}
