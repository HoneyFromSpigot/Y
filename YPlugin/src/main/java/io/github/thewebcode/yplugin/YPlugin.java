package io.github.thewebcode.yplugin;

import com.google.common.collect.Lists;
import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.chat.PrivateMessageManager;
import io.github.thewebcode.yplugin.chat.menu.ChatMenuCommandListener;
import io.github.thewebcode.yplugin.command.RegisterCommandMethodException;
import io.github.thewebcode.yplugin.command.commands.CleanCommand;
import io.github.thewebcode.yplugin.command.commands.DebugModeCommand;
import io.github.thewebcode.yplugin.command.commands.GadgetsCommand;
import io.github.thewebcode.yplugin.command.commands.SkullCommand;
import io.github.thewebcode.yplugin.config.YPluginYamlConfiguration;
import io.github.thewebcode.yplugin.config.Configuration;
import io.github.thewebcode.yplugin.file.TextFile;
import io.github.thewebcode.yplugin.item.ItemSetManager;
import io.github.thewebcode.yplugin.item.SavedItemManager;
import io.github.thewebcode.yplugin.listeners.*;
import io.github.thewebcode.yplugin.nms.NMS;
import io.github.thewebcode.yplugin.player.Players;
import io.github.thewebcode.yplugin.plugin.IYBukkitPlugin;
import io.github.thewebcode.yplugin.plugin.Plugins;
import io.github.thewebcode.yplugin.warp.Warps;
import io.github.thewebcode.yplugin.world.Worlds;
import io.github.thewebcode.yplugin.yml.InvalidConfigurationException;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class YPlugin extends IYBukkitPlugin {
    private static YPlugin plugin;

    public static final String WARP_DATA_FOLDER = "plugins/YPlugin/Warps/";
    public static final String PLUGIN_DATA_FOLDER = "plugins/YPlugin/";
    public static final String DEBUG_DATA_FOLDER = "plugins/YPlugin/Debug/";
    public static final String ITEM_DATA_FOLDER = "plugins/YPlugin/Items";
    public static final String ITEM_SET_DATA_FOLDER = "plugins/YPlugin/ItemSets/";
    public static final String RULES_LOCATION = "plugins/YPlugin/rules.txt";
    public static final String TELEPORT_MENU_DISABLED_LOCATION = "plugins/YPlugin/disabled-teleport-menus.txt";
    public static final String DATA_OPTION_FILE = "plugins/YPlugin/data-option.txt";

    private static Configuration globalConfig = null;

    private Worlds worlds;

    private Players players;

    private ItemSetManager itemSetManager = new ItemSetManager();

    private PrivateMessageManager privateMessageManager;

    private ChatMenuCommandListener chatMenuListener = null;

    public static synchronized YPlugin getInstance() {
        if (plugin == null) {
            plugin = (YPlugin) Plugins.getPlugin("YPlugin");
        }
        return plugin;
    }


    public void startup() {
        NMS.init();
        prepForCustomEnchantments();

        chatMenuListener = new ChatMenuCommandListener(this);
        privateMessageManager = new PrivateMessageManager();
        worlds = new Worlds();
        players = new Players();
        registerCommands(
                new DebugModeCommand(),
                new GadgetsCommand(),
                new SkullCommand(),
                new CleanCommand()
        );

        if (getConfiguration().registerCommands()) {
            try {
                registerCommandsByPackage("io.github.thewebcode.yplugin.command.commands");
            } catch (RegisterCommandMethodException e) {
                e.printStackTrace();
                debug("Unable to register commands; If you're using the no-commands version of YPlugin assure '<register-commands>' inside of Config.xml is set to false. Otherwise, send the stack trace to our developers for assistance.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            registerDebugActionsByPackage("io.github.thewebcode.yplugin.debug.actions");
        } catch (Exception e) {
            e.printStackTrace();
            Chat.debug("Unable to register all debug actions. To register via class and reflection it requires a constructor with 0 arguments");
        }

        registerListeners();
        Warps.loadWarps();
        for (Player player : Players.allPlayers()) {
            players.addData(player);
        }
    }

    @Override
    public void shutdown() {
        for (Player player : Players.allPlayers()) {
            UUID playerId = player.getUniqueId();
            Players.removeData(playerId);
        }
        Warps.saveWarps();
    }

    @Override
    public String getAuthor() {
        return "Brandon Curtis";
    }

    public static class TeleportMenuSettings {
        private List<String> disabledUuids = new ArrayList<>();

        private static TeleportMenuSettings instance;

        private TextFile textFile;

        public static TeleportMenuSettings getInstance() {
            return instance;
        }

        public static void init(String path) {
            instance = new TeleportMenuSettings(path);
        }

        protected TeleportMenuSettings(String filePath) {
            textFile = new TextFile(filePath);
        }

        public boolean hasMenuDisabled(UUID id) {
            return disabledUuids.contains(id.toString());
        }

        public void disableMenu(UUID id) {
            disabledUuids.add(id.toString());
            save();
        }

        public void enableMenu(UUID id) {
            disabledUuids.remove(id.toString());
            save();
        }

        private void save() {
            textFile.overwriteFile(disabledUuids);
        }
    }

    public static class Rules {
        private static List<String> rules = Lists.newArrayList(
                "1. You may not use vulgar or abusive language.",
                "2. You musn't be racist.",
                "3. You musn't use hacks or (unnapproved) mods that give you an unfair advantage",
                "4. You may not spam",
                "5. You may not advertise",
                "6. You musn't use excessive caps",
                "7. You may not advertise any links that are not Tunnels related",
                "8. You musn't abuse glitches or game exploits",
                "9. You may not troll any of the members, or ellicit ill behaviour in any way.",
                "10. You must be respectful to players",
                "11. Do not abuse glitches, and report them if found",
                "12. Do not steal, nor cheat the server or players",
                "13. AFK Machines are forbidden."
        );

        private static File file;

        private static Rules instance;

        public static void init(File file) {
            instance = new Rules(file);
        }

        public static Rules getInstance() {
            return instance;
        }

        protected Rules(File f) {
            file = f;
            if (!file.exists()) {
                try {
                    FileUtils.writeLines(file, rules);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }


            load();
        }

        public static void load() {
            try {
                rules = FileUtils.readLines(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public static void add(String rule) {
            rules.add(String.format("%s. %s", rules.size() + 1, rule));
            try {
                FileUtils.writeLines(file, rules, false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public static List<String> getRules() {
            return rules;
        }

    }

    public static int getServerId() {
        return 0;
    }

    private void prepForCustomEnchantments() {
        try {
            Field acceptingNew = Enchantment.class.getDeclaredField("acceptingNew");
            acceptingNew.setAccessible(true);
            acceptingNew.set(null, true);
            acceptingNew.setAccessible(false);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void registerListeners() {

        registerListeners(new ChatListener());
        debug("&aCreated the Chat Listener");

        if (globalConfig.hasLaunchpadPressurePlates()) {
            registerListeners(new LauncherListener());
            debug("&aRegistered the fire pad listener");
        }

        if (globalConfig.disableIceAccumulation() || globalConfig.disableSnowAccumulation()) {
            registerListeners(new BlockFormListener());
            debug("&aRegistered the block spread listener");
        }

        if (globalConfig.disableMyceliumSpread()) {
            registerListeners(new BlockSpreadListener());
            debug("&aRegistered the mycelium spread listener");
        }

        if (globalConfig.disableThunder()) {
            registerListeners(new ThungerChangeListener());
            debug("&aRegistered the thunder listener");
        }

        if (globalConfig.disableWeather()) {
            registerListeners(new WeatherChangeListener());
            debug("&aRegistered the Weather-Change listener");
        }

        if (globalConfig.disableLightning()) {
            registerListeners(new LightningStrikeListener());
            debug("&aRegistered the lightning listener");
        }

        if (globalConfig.disableFireSpread()) {
            registerListeners(new FireSpreadListener());
            debug("&aRegistered the fire-spread listener");
        }

        if (!globalConfig.enableItemPickup()) {
            registerListeners(new ItemPickupListener());
            debug("&aRegistered the item-pickup listener");
        }

        if (!globalConfig.enableFoodChange()) {
            registerListeners(new FoodChangeListener());
            debug("&aRegistered the food change listener");
        }

        registerListeners(
                new PlayerInteractListener(),
                new BlockBreakPlaceListener(),
                new EntityExplodeListener(),
                new WorldLoadedListener(),
                new ServerPingListener(),
                new PlayerLoginListener(),
                new PlayerJoinListener(),
                new PlayerKickListener(),
                new InventoryListener(),
                new PlayerTeleportListener(),
                new PlayerQuitListener(),
                new CommandPreProcessListener(),
                new PlayerDeathListener(),
                new ItemDropListener(),
                new EntityDamageEntityListener(),
                new ItemBreakListener(),
                new ItemDamageListener(),
                new EntityDamageListener(),
                new SignEditListener(),
                new LeavesDecayListener(),
                new PlayerSwapHandItemsListener()
        );
    }

    public void reloadConfiguration() {
        getInstance().initConfig();
    }

    public Configuration getConfiguration() {
        return globalConfig;
    }

    public ItemSetManager getItemSetManager() {
        return itemSetManager;
    }

    public Players getPlayerHandler() {
        return players;
    }

    public PrivateMessageManager getPrivateMessageManager() {
        return privateMessageManager;
    }

    public Worlds getWorldHandler() {
        return worlds;
    }

    public boolean isServerFull() {
        return Players.getOnlineCount() >= Bukkit.getMaxPlayers();
    }

    public ChatMenuCommandListener getChatMenuListener() {
        return chatMenuListener;
    }

    @Override
    public void initConfig() {
        File ymlConfigFile = new File(PLUGIN_DATA_FOLDER + "config.yml");
        File warpsFolder = new File(WARP_DATA_FOLDER);
        if (!warpsFolder.exists()) {
            warpsFolder.mkdirs();
        }
        File itemsFolder = new File(ITEM_DATA_FOLDER);
        if (!itemsFolder.exists()) {
            itemsFolder.mkdirs();
        }
        File itemSetsFolder = new File(ITEM_SET_DATA_FOLDER);
        if (!itemSetsFolder.exists()) {
            itemSetsFolder.mkdirs();
        }

        Collection<File> itemSetFiles = FileUtils.listFiles(itemSetsFolder, null, false);
        if (itemSetFiles.size() > 0) {
            for (File file : itemSetFiles) {
                try {
                    ItemSetManager.ItemSet set = new ItemSetManager.ItemSet(file);

                    set.load();

                    itemSetManager.addSet(set);
                    debug(String.format("Loaded itemset '%s' into the ItemSet Manager", set.getName()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        Collection<File> itemFiles = FileUtils.listFiles(itemsFolder, null, false);
        if (itemFiles.size() > 0) {
            for (File file : itemFiles) {
                SavedItemManager.loadItem(file);
            }
        }
        File debugFolder = new File(DEBUG_DATA_FOLDER);
        if (!debugFolder.exists()) {
            debugFolder.mkdirs();
        }
        Rules.init(new File(RULES_LOCATION));
        TeleportMenuSettings.init(TELEPORT_MENU_DISABLED_LOCATION);
        if (!ymlConfigFile.exists()) {

            globalConfig = new YPluginYamlConfiguration();
            YPluginYamlConfiguration ymlConfig = new YPluginYamlConfiguration();

            try {
                ymlConfig.init(ymlConfigFile);
                globalConfig = ymlConfig;
            } catch (InvalidConfigurationException e) {
                e.printStackTrace();
            }
            return;
        }

        globalConfig = new YPluginYamlConfiguration();
        try {
            ((YPluginYamlConfiguration) globalConfig).init(ymlConfigFile);
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
            debug(
                    "YPlugin 'plugin.yml' is invalid, and will cause",
                    "the plugin to malfunction without valid",
                    "configuration available....",
                    "Please check the formatting of your config file, or delete",
                    "it and regenerate it via restarting the server",
                    "to continue!",
                    "----------------",
                    "YPLUGIN HAS BEEN DISABLED",
                    "----------------");
            Plugins.disablePlugin(this);
            return;
        }

    }
}