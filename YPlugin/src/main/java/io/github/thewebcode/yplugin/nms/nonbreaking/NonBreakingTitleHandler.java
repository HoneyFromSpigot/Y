package io.github.thewebcode.yplugin.nms.nonbreaking;

import io.github.thewebcode.yplugin.nms.AbstractTitle;
import org.bukkit.entity.Player;

public class NonBreakingTitleHandler implements AbstractTitle.TitleHandler {
    @Override
    public void send(Player player, AbstractTitle title) {
        title.setTimingsToTicks();

        player.sendTitle(title.getTitle(),title.getSubtitle(),title.getFadeInTime(),title.getStayTime(),title.getFadeOutTime());
    }

    @Override
    public void resetTitle(Player player) {
        player.resetTitle();
    }

    @Override
    public void clearTitle(Player player) {
        player.resetTitle();
    }
}
