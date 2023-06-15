package io.github.thewebcode.yplugin.chat.menu;

import com.google.common.collect.Lists;
import io.github.thewebcode.yplugin.utilities.StringUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permissible;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HelpScreen {

    private HashMap<String, HelpScreenEntry> helpMenuMap = new HashMap<>();
    private final String menuName;
    private String menuHeader = "<name> Help (<page> / <maxpage>)";
    private String menuFormat = "<name> -> <desc>";
    private ChatColor oddItemColor = ChatColor.GRAY;
    private ChatColor evenItemColor = ChatColor.GRAY;

    public HelpScreen(String name) {
        menuName = name;
    }

    public void setFormat(String format) {
        this.menuFormat = format;
    }

    public void setFlipColor(ChatColor oddItemColor, ChatColor evenItemColor) {
        if (oddItemColor == null) {
            oddItemColor = ChatColor.GRAY;
        }

        if (evenItemColor == null) {
            evenItemColor = ChatColor.GRAY;
        }
        this.oddItemColor = oddItemColor;
        this.evenItemColor = evenItemColor;
    }

    public void setSimpleColor(ChatColor itemColor) {
        setFlipColor(itemColor, itemColor);
    }

    public void setHeader(String header) {
        if (header == null) {
            header = "";
        }
        this.menuHeader = header;
    }

    public void setEntry(String name, String description) {
        HelpScreenEntry entry = new HelpScreenEntry(name, description);
        helpMenuMap.put(name.toLowerCase(), entry);
    }

    public void setEntry(String name, String description, String... permissions) {
        HelpScreenEntry entry = new HelpScreenEntry(name, description);
        entry.setPermissions(permissions);
        helpMenuMap.put(name.toLowerCase(), entry);
    }

    public HelpScreen addEntry(String name, String item) {
        helpMenuMap.put(name.toLowerCase(), new HelpScreenEntry(name, item));
        return this;
    }

    public HelpScreen addEntry(String name, String item, String... permissions) {
        HelpScreenEntry entry = new HelpScreenEntry(name, item);
        entry.setPermissions(permissions);
        helpMenuMap.put(name.toLowerCase(), entry);
        return this;
    }

    public HelpScreenEntry getEntry(String name) {
        return helpMenuMap.get(name.toLowerCase());
    }

    public List<HelpScreenEntry> toSend(Permissible p) {
        ArrayList<HelpScreenEntry> toSend = new ArrayList<>();

        for (HelpScreenEntry e : helpMenuMap.values()) {
            if (e.isPermitted(p)) {
                toSend.add(e);
            }
        }

        return toSend;
    }

    public void sendTo(CommandSender s, int page, int perPage) {
        List<List<HelpScreenEntry>> helpScreenPages = Lists.partition(toSend(s), perPage);
        List<HelpScreenEntry> helpScreenEntries = helpScreenPages.get(page - 1);
        int entryAmount = helpScreenEntries.size();
        String[] messages = new String[entryAmount + 1];
        messages[0] = menuHeader.replaceAll("<name>", menuName).replaceAll("<page>", page + "").replaceAll("<maxpage>", helpScreenPages.size() + "");

        int i = 1;
        boolean col = false;
        for (HelpScreenEntry e : helpScreenEntries) {
            messages[i++] = ((col = !col) ? oddItemColor : evenItemColor) + e.fromFormat(menuFormat);
        }
        s.sendMessage(messages);
    }

    public class HelpScreenEntry {

        private final String NAME;
        private final String DESC;

        private String[] perms;

        public HelpScreenEntry(String name, String description) {
            this.NAME = name;
            this.DESC = description;
            this.perms = new String[0];
        }

        public void setPermissions(String... perms) {
            this.perms = perms;
        }

        public boolean isPermitted(Permissible p) {
            boolean b = true;
            for (String s : perms) {
                if (p.hasPermission(s)) {
                    b = true;
                    break;
                } else {
                    b = false;
                }
            }
            return b;
        }

        public String fromFormat(String format) {
            return StringUtil.formatColorCodes(format.replaceAll("<name>", NAME).replaceAll("<desc>", DESC));
        }

        @Override
        public String toString() {
            return NAME + " -> " + DESC;
        }
    }

}
