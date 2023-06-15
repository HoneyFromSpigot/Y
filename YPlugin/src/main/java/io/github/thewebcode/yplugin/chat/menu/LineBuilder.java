package io.github.thewebcode.yplugin.chat.menu;

import io.github.thewebcode.yplugin.YPlugin;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.ArrayList;
import java.util.List;

public class LineBuilder {

    public static LineBuilder of(String text) {
        return new LineBuilder(text);
    }

    BaseComponent base;
    protected ChatMenu container;
    protected List<String> listenerKeys = new ArrayList<>();

    public LineBuilder() {
        this("");
    }

    public LineBuilder(String text) {
        base = new TextComponent(text);
    }

    public LineBuilder append(String... text) {
        for (String s : text) {
            base.addExtra(s);
        }
        return this;
    }

    public LineBuilder append(BaseComponent... components) {
        for (BaseComponent component : components) {
            base.addExtra(component);
        }
        return this;
    }

    public LineBuilder append(ChatMenuComponent component) {
        component.appendTo(this);
        return this;
    }

    public LineBuilder append(ChatMenuListener listener, BaseComponent... components) {
        for (BaseComponent component : components) {
            String key = YPlugin.getInstance().getChatMenuListener().registerListener(listener);
            component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/chatmenu " + key));
            listenerKeys.add(key);

            base.addExtra(component);
        }
        return this;
    }

    protected LineBuilder withContainer(ChatMenu container) {
        this.container = container;
        return this;
    }

    public ChatMenu getContainer() {
        return this.container;
    }

    public BaseComponent build() {
        return this.base;
    }

}
