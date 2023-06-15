package io.github.thewebcode.yplugin.player;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import io.github.thewebcode.yplugin.YPlugin;
import io.github.thewebcode.yplugin.block.Blocks;
import io.github.thewebcode.yplugin.block.Direction;
import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.config.ColorCode;
import io.github.thewebcode.yplugin.effect.ParticleEffect;
import io.github.thewebcode.yplugin.entity.Entities;
import io.github.thewebcode.yplugin.game.gadget.Gadget;
import io.github.thewebcode.yplugin.game.gadget.Gadgets;
import io.github.thewebcode.yplugin.game.world.Arena;
import io.github.thewebcode.yplugin.inventory.*;
import io.github.thewebcode.yplugin.item.Items;
import io.github.thewebcode.yplugin.location.Locations;
import io.github.thewebcode.yplugin.nms.NMS;
import io.github.thewebcode.yplugin.permission.Perms;
import io.github.thewebcode.yplugin.sound.Sounds;
import io.github.thewebcode.yplugin.threading.tasks.KickPlayerThread;
import io.github.thewebcode.yplugin.threading.tasks.NameFetcherCallable;
import io.github.thewebcode.yplugin.threading.tasks.UuidFetcherCallable;
import io.github.thewebcode.yplugin.utilities.ListUtils;
import io.github.thewebcode.yplugin.utilities.NumberUtil;
import io.github.thewebcode.yplugin.utilities.StringUtil;
import io.github.thewebcode.yplugin.warp.Warp;
import io.github.thewebcode.yplugin.world.WorldHeight;
import io.github.thewebcode.yplugin.world.Worlds;
import org.apache.commons.lang.Validate;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Players {
    private static YPlugin yPlugin = YPlugin.getInstance();

    public static final int DEPTH_EQUALIZE_NUMBER = 63;
    private static final int MAX_BLOCK_TARGET_DISTANCE = 30;

    private static Map<UUID, MinecraftPlayer> playerData = new HashMap<>();

    private static Gson gson = new Gson();

    public boolean hasData(UUID playerId) {
        return playerData.containsKey(playerId);
    }

    public MinecraftPlayer getData(UUID playerId) {
        return playerData.get(playerId);
    }

    public MinecraftPlayer getData(Player player) {
        return playerData.get(player.getUniqueId());
    }

    @SuppressWarnings("deprecation")
    public void addData(Player player) {
        UUID playerId = player.getUniqueId();
        String playerName = player.getName();

        MinecraftPlayer minecraftPlayer;
        minecraftPlayer = new MinecraftPlayer(playerName);
        playerData.put(playerId, minecraftPlayer);
    }

    public static void removeData(UUID playerId) {
        Players players = yPlugin.getPlayerHandler();

        MinecraftPlayer wrapper = players.getData(playerId);

        if (wrapper == null) {
            return;
        }

        wrapper.dispose();
        playerData.remove(playerId);
    }

    public static boolean isOnline(String playerName) {
        return isOnline(playerName, false);
    }

    @SuppressWarnings("deprecation")
    public static boolean isOnlineExact(String playerName) {
        return Bukkit.getPlayerExact(playerName) != null;
    }

    public static boolean isOnline(String playerName, boolean isExact) {
        return isExact ? isOnlineExact(playerName) : isOnlineFuzzy(playerName);
    }

    @SuppressWarnings("deprecation")
    public static boolean isOnlineFuzzy(String playerName) {
        return getPlayer(playerName) != null;
    }

    public static boolean isOnline(UUID uniqueId) {
        return getPlayer(uniqueId) != null;
    }

    public static OfflinePlayer getOfflinePlayer(UUID id) {
        return Bukkit.getOfflinePlayer(id);
    }

    public static OfflinePlayer getOfflinePlayer(String name) {
        return Bukkit.getOfflinePlayer(name);
    }

    public static Set<Player> getPlayersWithPermission(String... permission) {
        return stream().filter(p -> {
            boolean pass = true;
            for (String s : permission) {
                if (!p.hasPermission(s)) {
                    pass = false;
                    break;
                }
            }
            return pass;
        }).collect(Collectors.toSet());
    }

    @Deprecated
    @SuppressWarnings("deprecation")
    public static Player getPlayer(String playerName) {
        for (Player player : allPlayers()) {
            if (playerName.equalsIgnoreCase(player.getName())) {
                return player;
            }
        }
        return null;
    }

    public static Player getPlayer(UUID uniqueId) {
        return Bukkit.getPlayer(uniqueId);
    }

    public static Player getPlayer(MinecraftPlayer minecraftPlayer) {
        return getPlayer(minecraftPlayer.getId());
    }

    @SuppressWarnings("deprecation")
    public static String getName(String partialPlayerName) {
        return isOnline(partialPlayerName) ? getPlayer(partialPlayerName).getName() : null;
    }

    public static String getNameFromUUID(UUID uuid) throws Exception {
        return NameFetcherCallable.getNameFromUUID(uuid);
    }

    public static String getNameFromUUID(String uuid) throws Exception {
        return getNameFromUUID(UUID.fromString(uuid));
    }

    public static UUID getUUIDFromName(String name) throws Exception {
        return UuidFetcherCallable.getUUIDOf(name);
    }

    public static UUID getUniqueId(Player player) {
        return player.getUniqueId();
    }


    public static void kick(Player player, String reason) {
        player.kickPlayer(StringUtil.formatColorCodes(reason));
    }


    public static void kick(Player player, String reason, boolean thread) {
        if (!thread) {
            kick(player, reason);
            return;
        }
        YPlugin.getInstance().getThreadManager().runTaskOneTickLater(new KickPlayerThread(player.getUniqueId(), reason));
    }

    public static void kickAll(String reason) {
        for (Player player : allPlayers()) {
            player.kickPlayer(StringUtil.formatColorCodes(reason));
        }
    }

    public static void kickAllWithoutPermission(String permission, String reason) {
        if (permission != null && !permission.isEmpty()) {
            for (Player player : allPlayers()) {
                if (!player.hasPermission(permission)) {
                    player.kickPlayer(StringUtil.formatColorCodes(reason));
                }
            }
        } else {
            kickAll(reason);
        }
    }

    public static void kickAllWithoutPermission(Perms permission, String reason) {
        kickAllWithoutPermission(permission.toString(), reason);
    }

    public static void setLevel(Player player, int lvl) {
        player.setLevel(lvl);
    }

    public static void removeLevel(Player player, int amount) {
        player.setLevel(player.getLevel() - amount);
    }

    public static void removeLevel(Player player) {
        removeLevel(player, 1);
    }

    public static void addLevel(Player player, int amount) {
        player.setLevel(player.getLevel() + amount);
    }

    public static void addLevel(Player player) {
        addLevel(player, 1);
    }

    public static void teleport(Player player, Entity target) {
        Validate.notNull(player);
        Validate.notNull(target);
        player.teleport(target, PlayerTeleportEvent.TeleportCause.PLUGIN);
    }

    public static void teleport(Player player, Location location) {
        Validate.notNull(player);
        Validate.notNull(location);

        float preYaw = player.getLocation().getYaw();
        float prePitch = player.getLocation().getPitch();

        player.teleport(new Location(location.getWorld(), location.getX(), location.getY(), location.getZ(), preYaw, prePitch), PlayerTeleportEvent.TeleportCause.PLUGIN);
    }

    public static void teleport(Player player, double[] xyz) {
        Validate.notNull(player);
        player.teleport(Locations.getLocation(player.getWorld(), xyz));
    }

    public static void teleport(Player player, Warp warp) {
        teleport(player, warp.getLocation());
    }

    public static void teleportToSpawn(Player player, World world) {
        teleport(player, Worlds.getSpawn(world));
    }

    public static void teleportToSpawn(Player player, Arena arena) {
        teleportToSpawn(player, arena.getWorld());
    }

    public static void teleportAllToSpawn(Arena arena) {
        stream().forEach(p -> teleportToSpawn(p, arena));
    }

    public static void chat(Player player, String message) {
        player.chat(StringUtil.colorize(message));
    }

    public static void allChat(String message) {
        for (Player player : allPlayers()) {
            chat(player, message);
        }
    }

    public static String getIPAddress(Player player) {
        return player.getAddress().getHostName();
    }

    public static boolean hasPermission(Player player, String permission) {
        return player.hasPermission(permission);
    }

    public static boolean hasPermission(Player player, Perms permission) {
        return hasPermission(player, permission.toString());
    }

    public static boolean hasPlayed(UUID playerId) {
        return getOfflinePlayer(playerId).hasPlayedBefore();
    }

    public static boolean hasPlayed(String name) {
        return getOfflinePlayer(name).hasPlayedBefore();
    }

    public static ChatColor getNameTagColor(Player player) {
        if (!player.isOp()) {
            for (ColorCode colorCode : ColorCode.values()) {
                if (player.hasPermission(colorCode.getPermission())) {
                    return colorCode.getColor();
                }
            }
        }
        return ChatColor.AQUA;
    }

    public static boolean canChatWhileSilenced(Player player) {
        return (hasPermission(player, Perms.BYPASS_CHAT_SILENCE));
    }

    public static void cycleGameMode(Player player) {
        switch (player.getGameMode()) {
            case SURVIVAL:
                player.setGameMode(GameMode.CREATIVE);
                break;
            case CREATIVE:
                player.setGameMode(GameMode.ADVENTURE);
                break;
            case ADVENTURE:
                player.setGameMode(GameMode.SURVIVAL);
                break;
            case SPECTATOR:
                player.setGameMode(GameMode.SPECTATOR);
            default:
                player.setGameMode(GameMode.SURVIVAL);
                break;
        }
    }

    public static void clearInventory(Player player) {
        clearInventory(player, true);
    }

    public static void setInventory(Player player, Map<Integer, ItemStack> itemMap, boolean clearInventory) {
        if (clearInventory) {
            clearInventory(player, true);
        }

        PlayerInventory inventory = player.getInventory();
        for (Map.Entry<Integer, ItemStack> itemEntry : itemMap.entrySet()) {
            inventory.setItem(itemEntry.getKey(), itemEntry.getValue());
        }
    }

    public static void clearInventory(Player player, boolean clearArmor) {
        player.getInventory().clear();
        if (clearArmor) {
            player.getInventory().setArmorContents(new ItemStack[]{null, null, null, null});
        }
        player.updateInventory();
    }

    public static void updateInventory(Player player) {
        yPlugin.getThreadManager().runTaskLater(player::updateInventory, 1);
    }

    public static void dropInventory(Player player) {
        ItemStack[] inventoryContents = player.getInventory().getContents();
        Players.clearInventory(player);

        for (ItemStack item : inventoryContents) {
            if (item == null) {
                continue;
            }
            Worlds.dropItemNaturally(player, item);
        }
    }

    public static void giveItem(Player player, ItemStack itemStack) {
        player.getInventory().addItem(itemStack);
    }

    public static boolean giveItem(Player player, ItemStack itemStack, boolean drop) {
        PlayerInventory inventory = player.getInventory();
        if (inventory.firstEmpty() == -1) {
            if (drop) {
                Worlds.dropItem(player, itemStack, false);
                return true;
            }
            return false;
        }
        inventory.addItem(itemStack);
        return true;
    }

    @Nullable
    public static ItemStack getItem(Player player, int slot) {
        return player.getInventory().getItem(slot);
    }

    public static void setItem(Player player, int slot, ItemStack item) {
        Inventories.setItem(player.getInventory(), slot, item);
        player.updateInventory();
    }

    public static void setHotbarSelection(Player player, int slot) {
        if (slot > 8) {
            return;
        }

        player.getInventory().setHeldItemSlot(slot);
    }

    public static void setHotbarItem(Player player, ItemStack item, int slot) {
        if (slot > 8) {
            return;
        }

        setItem(player, slot, item);
    }

    public static void setHotbarContents(Player player, ItemStack... items) {
        for (int i = 0; i < items.length; i++) {
            setHotbarItem(player, items[i], i);
        }
    }

    public static void setHotbar(Player player, Hotbar hotbar) {
        hotbar.assign(player);
    }

    public static void giveItem(Player player, ItemStack... items) {
        for (ItemStack itemStack : items) {
            giveItem(player, itemStack, true);
        }
    }

    public static void setArmor(Player player, ArmorSlot armorSlot, ItemStack itemStack) {
        if (itemStack == null || armorSlot == null) {
            return;
        }

        PlayerInventory inventory = player.getInventory();
        switch (armorSlot) {
            case HELMET:
                inventory.setHelmet(itemStack);
                break;
            case CHEST:
                inventory.setChestplate(itemStack);
                break;
            case LEGGINGS:
                inventory.setLeggings(itemStack);
                break;
            case BOOTS:
                inventory.setBoots(itemStack);
                break;
            case MAIN_HAND:
                inventory.setItemInMainHand(itemStack);
                break;
            case OFF_HAND:
                inventory.setItemInOffHand(itemStack);
                break;
            default:
                break;
        }
    }

    public static ItemStack[] getArmor(Player player) {
        return player.getInventory().getArmorContents();
    }

    public static ItemStack getArmor(Player player, ArmorSlot armorSlot) {
        PlayerInventory playerInventory = player.getInventory();
        ItemStack itemStack = null;
        switch (armorSlot) {
            case HELMET:
                itemStack = playerInventory.getHelmet();
                break;
            case CHEST:
                itemStack = playerInventory.getChestplate();
                break;
            case LEGGINGS:
                itemStack = playerInventory.getLeggings();
                break;
            case BOOTS:
                itemStack = playerInventory.getBoots();
                break;
            case MAIN_HAND:
                itemStack = playerInventory.getItemInMainHand();
                break;
            case OFF_HAND:
                itemStack = playerInventory.getItemInOffHand();
                break;
            default:
                break;
        }
        return itemStack;
    }

    public static void setArmor(Player player, ItemStack[] armor) {
        player.getInventory().setArmorContents(armor);
    }

    public static void setArmor(Player player, ArmorInventory armor) {
        for (Map.Entry<ArmorSlot, ItemStack> entry : armor.getArmor().entrySet()) {
            setArmor(player, entry.getKey(), entry.getValue());
        }
    }

    public static void removePotionEffects(Player player) {
        Entities.removePotionEffects(player);
    }

    public static void addPotionEffect(Player player, PotionEffect potionEffect) {
        Entities.addPotionEffect(player, potionEffect);
    }

    public static int getOnlineCount() {
        return allPlayers().size();
    }

    public static Set<Player> allPlayers() {
        return Sets.newHashSet(Bukkit.getOnlinePlayers());
    }

    public static Stream<Player> stream() {
        return allPlayers().stream();
    }

    public static Set<Player> onlineOperators() {
        Set<Player> players = new HashSet<>();
        for (Player player : allPlayers()) {
            if (player.isOp()) {
                players.add(player);
            }
        }
        return players;
    }


    public static Player getRandomPlayer() {
        return ListUtils.getRandom(Lists.newArrayList(allPlayers()));
    }

    public static Collection<Player> allPlayers(World world) {
        return world.getEntitiesByClass(Player.class);
    }

    public static Collection<MinecraftPlayer> allPlayerWrappers() {
        return playerData.values();
    }

    public static Set<Player> getAllDebugging() {
        Set<Player> players = new HashSet<>();
        for (MinecraftPlayer wrapper : playerData.values()) {
            if (wrapper.isOnline() && wrapper.isInDebugMode()) {
                players.add(getPlayer(wrapper));
            }
        }
        return players;
    }

    public static boolean isDebugging(Player player) {
        return yPlugin.getPlayerHandler().getData(player).isInDebugMode();
    }

    public static Set<Player> getPlayers(Collection<UUID> ids) {
        Set<Player> players = new HashSet<>();
        for (UUID id : ids) {
            if (!isOnline(id)) {
                continue;
            }
            players.add(getPlayer(id));
        }
        return players;
    }

    @SuppressWarnings("deprecation")
    public static Set<Player> allPlayersExcept(String... excludedPlayers) {
        Set<Player> players = new HashSet<>();
        Set<String> names = Sets.newHashSet(excludedPlayers);
        for (Player player : allPlayers()) {
            if (!names.contains(player.getName())) {
                players.add(player);
            }
        }
        return players;
    }

    public static Set<Player> allPlayersExcept(UUID... playerIds) {
        Set<Player> players = Sets.newHashSet(allPlayers());
        Set<UUID> uniqueIds = Sets.newHashSet(playerIds);
        for (Player player : allPlayers()) {
            if (uniqueIds.contains(player.getUniqueId())) {
                players.remove(player);
            }
        }
        return players;
    }

    public static boolean isOnline(int amount) {
        return getOnlineCount() >= amount;
    }

    public static int getDepth(Player player) {
        return player.getLocation().getBlockY();
    }

    public static WorldHeight getWorldHeight(Player player) {
        int equalizedDepth = getEqualizedDepth(player);
        if (equalizedDepth > 0) {
            return WorldHeight.ABOVE_SEA_LEVEL;
        } else if (equalizedDepth < 0) {
            return WorldHeight.BELOW_SEA_LEVEL;
        } else {
            return WorldHeight.AT_SEA_LEVEL;
        }
    }

    private static int getEqualizedDepth(Player player) {
        return getDepth(player) - DEPTH_EQUALIZE_NUMBER;
    }

    public static boolean isAboveSeaLevel(Player player) {
        return getEqualizedDepth(player) > 0;
    }

    public static boolean isBelowSeaLevel(Player player) {
        return getEqualizedDepth(player) < 0;
    }

    public static boolean isAtSeaLevel(Player player) {
        return getEqualizedDepth(player) == 0;
    }

    public static void feed(Player player, int amount) {
        player.setFoodLevel(amount);
        player.setSaturation(10);
        player.setExhaustion(0);
    }

    public static void feed(Player player) {
        feed(player, 20);
    }

    public static void decreaseHunger(Player player, int amount) {
        int hungerLevel = player.getFoodLevel();
        hungerLevel -= amount;
        if (hungerLevel <= 0) {
            hungerLevel = 0;
        }

        player.setFoodLevel(hungerLevel);
    }

    public static void restoreHealth(Player p, int amount) {
        double currentHealth = p.getHealth();
        double maxHealth = p.getMaxHealth();

        if (currentHealth >= maxHealth) {
            return;
        }

        double newHealth = currentHealth + amount;
        if (newHealth > maxHealth) {
            newHealth = maxHealth;
        }

        p.setHealth(newHealth);
    }

    public static void restoreHealth(Player p, double amount) {
        double currentHealth = p.getHealth();
        double maxHealth = p.getMaxHealth();

        if (currentHealth >= maxHealth) {
            return;
        }

        double newHealth = currentHealth + amount;
        if (newHealth > maxHealth) {
            newHealth = maxHealth;
        }

        p.setHealth(newHealth);
    }

    public static void heal(Player player) {
        Players.removePotionEffects(player);
        removeFire(player);
        Entities.setHealth(player, Entities.getMaxHealth(player));
    }

    public static void removeFire(Player player) {
        Entities.removeFire(player);
    }

    public static void repairItems(Player player) {
        repairItems(player, false);
    }

    public static void repairItems(Player player, boolean repairArmor) {
        PlayerInventory inventory = player.getInventory();
        Items.repairItems(inventory.getContents());
        if (repairArmor) {
            Items.repairItems(inventory.getArmorContents());
        }
    }

    public static boolean hasItemInHand(Player player) {
        PlayerInventory playerInv = player.getInventory();
        ItemStack mainHand = playerInv.getItemInMainHand();
        ItemStack offHand = playerInv.getItemInOffHand();

        return (mainHand != null && mainHand.getType() != Material.AIR) || (offHand != null && offHand.getType() != Material.AIR);
    }

    public static boolean hasItemInHand(Player player, HandSlot slot) {
        switch (slot) {
            case MAIN_HAND:
                ItemStack mainHand = player.getInventory().getItemInMainHand();
                return mainHand != null && mainHand.getType() != Material.AIR;
            case OFF_HAND:
                ItemStack offHand = player.getInventory().getItemInOffHand();
                return offHand != null && offHand.getType() != Material.AIR;
            default:
                return false;
        }
    }

    public static ItemStack getItemInHand(Player player, HandSlot slot) {
        switch (slot) {
            case MAIN_HAND:
                return player.getInventory().getItemInMainHand();
            case OFF_HAND:
                return player.getInventory().getItemInOffHand();
            default:
                return null;
        }
    }

    public static void setItemInHand(Player player, ItemStack stack, HandSlot slot) {
        switch (slot) {
            case MAIN_HAND:
                player.getInventory().setItemInMainHand(stack);
                break;
            case OFF_HAND:
                player.getInventory().setItemInOffHand(stack);
                break;
            default:
                break;
        }
    }


    @Deprecated
    public static boolean hasItemInHand(Player player, ItemStack compare) {
        PlayerInventory inv = player.getInventory();

        ItemStack mainHand = inv.getItemInMainHand();
        ItemStack offHand = inv.getItemInOffHand();


        return mainHand.isSimilar(compare) || offHand.isSimilar(compare);
    }

    public static boolean hasItemInHand(Player player, ItemStack compare, HandSlot slot) {
        return hasItemInHand(player, slot) && getItemInHand(player, slot).isSimilar(compare);
    }

    @Deprecated
    public static boolean handIsEmpty(Player player) {
        return !hasItemInHand(player);
    }

    public static boolean handsAreEmpty(Player player) {
        return !hasItemInHand(player, HandSlot.MAIN_HAND) && !hasItemInHand(player, HandSlot.OFF_HAND);
    }

    public static boolean handIsEmpty(Player player, HandSlot slot) {
        return !hasItemInHand(player, slot);
    }

    public static void clearHands(Player player) {
        PlayerInventory inv = player.getInventory();

        inv.setItemInMainHand(null);
        inv.setItemInOffHand(null);
    }

    public static void clearHand(Player player, HandSlot slot) {
        switch (slot) {
            case MAIN_HAND:
                player.getInventory().setItemInMainHand(null);
                break;
            case OFF_HAND:
                player.getInventory().setItemInOffHand(null);
                break;
            default:
                break;
        }
    }

    @Deprecated
    public static void removeFromHand(Player player, int amount) {
        removeFromHand(player, amount, HandSlot.MAIN_HAND);
    }

    public static void removeFromHand(Player player, int amount, HandSlot slot) {
        if (!hasItemInHand(player, slot)) {
            return;
        }

        ItemStack handItem = Items.removeFromStack(getItemInHand(player, slot), amount);

        if (handItem == null || handItem.getType() == Material.AIR || handItem.getAmount() == 0) {
            Chat.debug(String.format("removeFromHand :: hand item is %s, amount is %s", handItem == null ? "null" : handItem.getType().name(), handItem == null ? "0" : handItem.getAmount()));
        }

        switch (slot) {
            case MAIN_HAND:
                player.getInventory().setItemInMainHand(handItem);
                break;
            case OFF_HAND:
                player.getInventory().setItemInOffHand(handItem);
                break;
            default:
                break;
        }

        updateInventory(player);
    }

    public static boolean hasItem(Player player, Material material, String name) {
        return Inventories.contains(player.getInventory(), material, name);
    }

    public static boolean hasItem(Player player, ItemStack item) {
        return Inventories.contains(player.getInventory(), item);
    }

    public static boolean hasItem(Player player, Material material) {
        return Inventories.contains(player.getInventory(), material);
    }

    public static boolean hasGadget(Player player, Gadget gadget) {
        return Players.hasItem(player, gadget.getItem());
    }

    @Deprecated
    public static boolean hasGadgetInHand(Player player) {
        return Gadgets.isGadget(getItemInHand(player, HandSlot.MAIN_HAND));
    }

    public static boolean hasGadgetInHand(Player player, HandSlot slot) {
        return Gadgets.isGadget(getItemInHand(player, slot));
    }

    public static void hidePlayer(Player player, Player target) {
        hidePlayer(player, target, ParticleEffect.SMOKE_LARGE);
    }

    public static void hidePlayer(Player player, Player target, ParticleEffect particles) {
        player.hidePlayer(target);
        ParticleEffect.sendToLocation(particles, target.getLocation(), NumberUtil.getRandomInRange(4, 7));
    }

    public static void hidePlayers(Player player, Collection<Player> targets) {
        targets.forEach(player::hidePlayer);
    }

    public static void unhidePlayers(Player player, Collection<Player> targets) {
        targets.forEach(player::showPlayer);
    }

    public static void hidePlayer(Player target, Collection<Player> players) {
        players.forEach(p -> p.hidePlayer(target));
    }


    public static void unhidePlayer(Player target, Collection<Player> players) {
        players.forEach(p -> p.showPlayer(target));
    }

    public static void hidePlayers(Player player) {
        for (Player p : allPlayers()) {
            player.hidePlayer(p);
        }
        yPlugin.getPlayerHandler().getData(player).setHidingOtherPlayers(true);
    }

    public static void unhidePlayers(Player player) {
        for (Player p : allPlayers()) {
            player.showPlayer(p);
        }
        yPlugin.getPlayerHandler().getData(player).setHidingOtherPlayers(false);
    }


    public static Location getTargetLocation(Player player) {
        return getTargetLocation(player, MAX_BLOCK_TARGET_DISTANCE);
    }

    public static Location getTargetLocation(Player player, int distance) {
        return Locations.getNormalizedLocation(player.getTargetBlock(Blocks.TRANSPARENT_MATERIALS, distance).getLocation());
    }

    public static String getWorldName(Player player) {
        return Worlds.getWorldName(player);
    }

    public static void playSoundAll(Sound sound, float volume, float pitch) {
        for (Player p : allPlayers()) {
            Sounds.playSound(p, sound, volume, pitch);
        }
    }

    public static void playSoundAll(Sound sound, int volume) {
        playSoundAll(sound, volume, 1.0f);
    }

    public static Direction getCardinalDirection(Player player) {
        double rot = (player.getLocation().getYaw() - 90) % 360;
        if (rot < 0) {
            rot += 360.0;
        }
        return getDirection(rot);
    }

    public static Direction getDirection(Player player) {
        int dirCode = Math.round(player.getLocation().getYaw() / 90f);
        switch (dirCode) {
            case 0:
                return Direction.SOUTH;
            case 1:
                return Direction.WEST;
            case 2:
                return Direction.NORTH;
            case 3:
                return Direction.EAST;
            default:
                return Direction.SOUTH;
        }
    }

    private static Direction getDirection(double rot) {
        if (0 <= rot && rot < 22.5) {
            return Direction.NORTH;
        } else if (22.5 <= rot && rot < 67.5) {
            return Direction.NORTHEAST;
        } else if (67.5 <= rot && rot < 112.5) {
            return Direction.EAST;
        } else if (112.5 <= rot && rot < 157.5) {
            return Direction.SOUTHEAST;
        } else if (157.5 <= rot && rot < 202.5) {
            return Direction.SOUTH;
        } else if (202.5 <= rot && rot < 247.5) {
            return Direction.SOUTHWEST;
        } else if (247.5 <= rot && rot < 292.5) {
            return Direction.WEST;
        } else if (292.5 <= rot && rot < 337.5) {
            return Direction.NORTHWEST;
        } else if (337.5 <= rot && rot < 360.0) {
            return Direction.NORTH;
        } else {
            return null;
        }
    }

    public static void setSpeed(Player p, int multiplier) {
        double walkSpeed = MinecraftPlayer.DEFAULT_WALK_SPEED * multiplier;
        double flySpeed = MinecraftPlayer.DEFAULT_FLY_SPEED * multiplier;
        p.setWalkSpeed((float) walkSpeed);
        p.setFlySpeed((float) flySpeed);
    }

    public static void resetSpeed(Player p) {
        p.setWalkSpeed((float) MinecraftPlayer.DEFAULT_WALK_SPEED);
        p.setFlySpeed((float) MinecraftPlayer.DEFAULT_FLY_SPEED);
    }

    public static void forceRespawn(Player p) {
        if (p == null || !p.isDead()) {
            return;
        }

        Bukkit.getScheduler().scheduleSyncRepeatingTask(YPlugin.getInstance(), new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    NMS.getForceRespawnHandler().forceRespawn(p);
                    cancel();
                } catch (ConcurrentModificationException e) {
                }
            }
        }, 5l, 5l);
    }
}
