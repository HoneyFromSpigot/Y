package io.github.thewebcode.yplugin.chat.menu;

import net.md_5.bungee.api.chat.TextComponent;

public abstract class ChatMenuComponent {

    TextComponent component = new TextComponent();

    public ChatMenuComponent() {
    }

    protected void updateText() {
        component.setText(render());
    }

    public String render() {
        return "";
    }

    public abstract ChatMenuComponent appendTo(LineBuilder builder);

}
