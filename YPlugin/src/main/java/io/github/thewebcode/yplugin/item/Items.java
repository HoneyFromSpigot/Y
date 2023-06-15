package io.github.thewebcode.yplugin.item;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.github.thewebcode.yplugin.Messages;
import io.github.thewebcode.yplugin.YPlugin;
import io.github.thewebcode.yplugin.block.Blocks;
import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.enchantments.GlowingEnchant;
import io.github.thewebcode.yplugin.exceptions.InvalidMaterialNameException;
import io.github.thewebcode.yplugin.reflection.ReflectionUtilities;
import io.github.thewebcode.yplugin.utilities.ListUtils;
import io.github.thewebcode.yplugin.utilities.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.Dye;
import org.bukkit.material.MaterialData;

import java.lang.reflect.Method;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class Items {

    public static final ItemStack[] DIAMOND_ARMOR = new ItemStack[]{
            makeItem(Material.DIAMOND_BOOTS),
            makeItem(Material.DIAMOND_LEGGINGS),
            makeItem(Material.DIAMOND_CHESTPLATE),
            makeItem(Material.DIAMOND_HELMET),
    };

    public static final ItemStack[] CHAIN_ARMOR = new ItemStack[]{
            makeItem(Material.CHAINMAIL_BOOTS),
            makeItem(Material.CHAINMAIL_LEGGINGS),
            makeItem(Material.CHAINMAIL_CHESTPLATE),
            makeItem(Material.CHAINMAIL_HELMET)
    };

    public static final ItemStack[] GOLD_ARMOR = new ItemStack[]{
            makeItem(Material.GOLDEN_BOOTS),
            makeItem(Material.GOLDEN_LEGGINGS),
            makeItem(Material.GOLDEN_CHESTPLATE),
            makeItem(Material.GOLDEN_HELMET)
    };

    public static final ItemStack[] IRON_ARMOR = new ItemStack[]{
            makeItem(Material.IRON_BOOTS),
            makeItem(Material.IRON_LEGGINGS),
            makeItem(Material.IRON_CHESTPLATE),
            makeItem(Material.IRON_HELMET)
    };

    public static final ItemStack[] LEATHER_ARMOR = new ItemStack[]{
            makeItem(Material.LEATHER_BOOTS),
            makeItem(Material.LEATHER_LEGGINGS),
            makeItem(Material.LEATHER_CHESTPLATE),
            makeItem(Material.LEATHER_HELMET)
    };

    private static Set<Material> armorMaterials = Sets.newHashSet(
            Material.LEATHER_BOOTS,
            Material.LEATHER_CHESTPLATE,
            Material.LEATHER_HELMET,
            Material.LEATHER_LEGGINGS,
            Material.GOLDEN_BOOTS,
            Material.GOLDEN_CHESTPLATE,
            Material.GOLDEN_HELMET,
            Material.GOLDEN_LEGGINGS,
            Material.IRON_BOOTS,
            Material.IRON_CHESTPLATE,
            Material.IRON_HELMET,
            Material.IRON_LEGGINGS,
            Material.DIAMOND_BOOTS,
            Material.DIAMOND_CHESTPLATE,
            Material.DIAMOND_HELMET,
            Material.DIAMOND_LEGGINGS
    );

    private static final Method TO_NMS = ReflectionUtilities.getMethod(ReflectionUtilities.getCBClass("inventory.CraftItemStack"), "asNMSCopy", ItemStack.class);

    static {
        armorMaterials = Sets.newHashSet();
        Collections.addAll(armorMaterials);
    }


    public static Object toNMS(ItemStack stack) {
        return ReflectionUtilities.invokeMethod(TO_NMS, null, stack);
    }

    public static boolean hasEnchantments(ItemStack itemStack) {
        return hasMetadata(itemStack) && getMetadata(itemStack).hasEnchants();
    }

    public static boolean hasMetadata(ItemStack itemStack) {
        if (itemStack == null) {
            return false;
        }

        return itemStack.hasItemMeta();
    }

    public static ItemMeta getMetadata(ItemStack itemStack) {
        return itemStack.getItemMeta();
    }

    public static ItemStack setMetadata(ItemStack itemStack, ItemMeta itemMeta) {
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static void clearLore(ItemStack item) {
        setLore(item, Arrays.asList());
    }

    public static String getLore(ItemStack itemStack, int line) {
        if (!hasLore(itemStack)) {
            return null;
        }
        try {
            return getLore(itemStack).get(line);
        } catch (IndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static ItemStack addLore(ItemStack itemStack, String... loreLines) {
        return addLore(itemStack, Arrays.asList(loreLines));
    }

    public static ItemStack addLore(ItemStack itemStack, List<String> loreLines) {
        ItemMeta itemMeta = getMetadata(itemStack);
        List<String> newItemLore = hasLore(itemMeta) ? getLore(itemMeta) : new ArrayList<>();
        for (String line : loreLines) {
            if (line == null) {
                continue;
            }

            newItemLore.add(StringUtil.formatColorCodes(line));
        }
        itemMeta = setLore(itemMeta, newItemLore);
        return setMetadata(itemStack, itemMeta);
    }

    public static boolean hasLore(ItemStack itemStack) {
        return hasMetadata(itemStack) && getMetadata(itemStack).hasLore();
    }

    public static boolean hasLore(ItemMeta itemMeta) {
        return itemMeta.hasLore();
    }

    public static List<String> getLore(ItemStack itemStack) {
        if (!hasLore(itemStack)) {
            return null;
        }
        return getMetadata(itemStack).getLore();
    }

    public static List<String> getLore(ItemMeta itemMeta) {
        return itemMeta.getLore();
    }

    public static boolean hasLoreAtLine(ItemStack item, int line) {
        List<String> loreLines = getLore(item);

        if (loreLines == null) {
            return false;
        }

        if (loreLines.size() > line) {
            return StringUtils.isNotEmpty(loreLines.get(line));
        }

        return false;
    }

    public static String getLoreLineContaining(ItemStack item, String text) {
        if (!hasLore(item)) {
            return null;
        }

        List<String> lore = getLore(item);

        String cLine = null;

        for (String line : lore) {
            if (!StringUtil.stripColor(line.toLowerCase()).contains(StringUtil.stripColor(text.toLowerCase()))) {
                continue;
            }

            cLine = line;
            break;
        }

        return cLine;
    }

    public static int getLoreLineNumberContaining(ItemStack item, String text) {
        if (!hasLore(item)) {
            return -1;
        }

        List<String> lore = getLore(item);

        for (int i = 0; i < lore.size(); i++) {
            String loreLine = StringUtil.stripColor(lore.get(i));

            if (!loreLine.toLowerCase().contains(StringUtil.stripColor(text.toLowerCase()))) {
                continue;
            }

            return i;
        }

        return -1;
    }

    public static List<String> getLoreLinesContaining(ItemStack item, String text) {
        List<String> lines = new ArrayList<>();

        if (!hasLore(item)) {
            return lines;
        }

        List<String> lore = getLore(item);

        if (lore == null) {
            return lines;
        }

        for (String line : lore) {
            if (StringUtil.stripColor(line).toLowerCase().contains(StringUtil.stripColor(text.toLowerCase()))) {
                lines.add(line);
            }
        }

        return lines;
    }

    public static void setLore(ItemStack item, int line, String lore) {
        if (!hasLoreAtLine(item, line)) {
            return;
        }

        List<String> loreLines = getLore(item);
        loreLines.set(line, StringUtil.formatColorCodes(lore));
        setLore(item, loreLines);
    }

    public static ItemStack setLore(ItemStack itemStack, List<String> loreLines) {
        ItemMeta itemMeta = getMetadata(itemStack);
        List<String> lore = new ArrayList<>();
        for (String line : loreLines) {
            if (line != null) {
                lore.add(StringUtil.formatColorCodes(line));
            }
        }
        itemMeta.setLore(lore);
        setMetadata(itemStack, itemMeta);
        return itemStack;
    }

    public static ItemMeta setLore(ItemMeta itemMeta, List<String> loreLines) {
        itemMeta.setLore(loreLines.stream().map(StringUtil::formatColorCodes).collect(Collectors.toList()));
        return itemMeta;
    }

    public static boolean loreContains(ItemStack itemStack, String text) {
        if (!hasLore(itemStack)) {
            return false;
        }

        List<String> itemLore = getLore(itemStack);
        int i = 0;
        for (String s : itemLore) {
            i++;
            if (s == null || s.isEmpty()) {
                continue;
            }
            if (StringUtil.stripColor(s.toLowerCase()).contains(StringUtil.stripColor(text.toLowerCase()))) {
                return true;
            }
        }
        return false;
    }

    public static ItemStack setName(ItemStack item, String name) {
        ItemMeta meta = getMetadata(item);
        meta = setName(meta, name);
        return setMetadata(item, meta);
    }

    public static ItemMeta setName(ItemMeta meta, String name) {
        meta.setDisplayName(StringUtil.formatColorCodes(name));
        return meta;
    }

    public static String getName(ItemStack itemStack) {
        if (itemStack == null) {
            throw new NullPointerException("Unable to get the name of a null item");
        }

        if (!hasName(itemStack)) {
            return getFormattedMaterialName(itemStack);
        }
        return StringUtil.stripColor(getMetadata(itemStack).getDisplayName());
    }

    public static boolean hasName(ItemStack itemStack) {
        return (hasMetadata(itemStack) && getMetadata(itemStack).hasDisplayName());
    }

    public static boolean hasMaterialData(ItemStack item, int id) {
        return item.getData().getData() == id;
    }

    public static boolean nameContains(ItemStack item, String text) {
        return StringUtils.containsIgnoreCase(getName(item), text);
    }

    public static ItemStack removeFromStack(ItemStack itemStack, int amount) {
        int itemStackAmount = itemStack.getAmount();

        if (itemStackAmount <= amount) {
            return null;
        }

        itemStack.setAmount(itemStackAmount - amount);
        return itemStack;
    }

    public static ItemStack setColor(ItemStack itemStack, Color color) {
        switch (itemStack.getType()) {
            case LEATHER_BOOTS:
            case LEATHER_CHESTPLATE:
            case LEATHER_HELMET:
            case LEATHER_LEGGINGS:
                LeatherArmorMeta itemMeta = (LeatherArmorMeta) getMetadata(itemStack);
                itemMeta.setColor(color);
                return setMetadata(itemStack, itemMeta);
            default:
                return itemStack;
        }
    }


    public static boolean addEnchantment(ItemStack itemStack, Enchantment enchantment, int enchantmentLevel, boolean ignoreRestrictions) {
        ItemMeta meta = getMetadata(itemStack);
        boolean enchanted = meta.addEnchant(enchantment, enchantmentLevel, ignoreRestrictions);
        setMetadata(itemStack, meta);
        return enchanted;
    }

    public static boolean addUnsafeEnchantment(ItemStack itemStack, Enchantment enchantment, int enchantmentLevel) {
        itemStack.addUnsafeEnchantment(enchantment, enchantmentLevel);
        boolean enchanted = Items.hasEnchantment(itemStack, enchantment);
        return enchanted;
    }

    public static boolean addEnchantment(ItemStack itemStack, Enchantment enchantment, int enchantmentLevel) {
        return addEnchantment(itemStack, enchantment, enchantmentLevel, false);
    }

    public static void addEnchantment(ItemStack target, ItemEnchantmentWrapper... enchants) {
        for (ItemEnchantmentWrapper wrapper : enchants) {
            addEnchantment(target, wrapper.getEnchantment(), wrapper.getLevel());
        }

    }

    public static void addEnchantments(ItemStack target, Collection<ItemEnchantmentWrapper> wrappers) {
        for (ItemEnchantmentWrapper wrapper : wrappers) {
            addEnchantment(target, wrapper.getEnchantment(), wrapper.getLevel());
        }
    }

    public static void clearEnchantments(ItemStack item) {
        getMetadata(item).getEnchants().clear();
    }

    public static void setEnchantments(ItemStack target, Set<ItemEnchantmentWrapper> enchants) {
        ItemMeta meta = getMetadata(target);
        meta.getEnchants().clear();

        for (ItemEnchantmentWrapper wrapper : enchants) {
            addEnchantment(target, wrapper.getEnchantment(), wrapper.getLevel());
        }
    }

    public static ItemStack replaceInName(ItemStack target, String search, String replace) {
        if (!hasName(target)) {
            return target;
        }

        String name = getName(target);

        if (StringUtils.containsIgnoreCase(search, replace)) {
            name = StringUtils.replace(name, search, replace);
        }

        return Items.setName(target, name);
    }

    public static boolean hasSameEnchantments(ItemStack item, ItemStack compareItem) {
        if (!Items.hasEnchantments(item) || !Items.hasEnchantments(compareItem)) {
            return false;
        }

        Map<Enchantment, Integer> enchantments = item.getEnchantments();
        Map<Enchantment, Integer> checkEnchants = compareItem.getEnchantments();
        if (enchantments.size() != checkEnchants.size()) {
            return false;
        }
        for (Entry<Enchantment, Integer> enchantmentEntry : checkEnchants.entrySet()) {
            Enchantment enchantment = enchantmentEntry.getKey();
            if (enchantments.containsKey(enchantment) && enchantments.get(enchantment).equals(enchantmentEntry.getValue())) {
                continue;
            }
            return false;
        }
        return true;
    }

    public static boolean hasEnchantment(ItemStack item, Enchantment enchant) {
        if (!Items.hasEnchantments(item)) {
            return false;
        }


        Set<ItemEnchantmentWrapper> enchants = getEnchantments(item);
        for (ItemEnchantmentWrapper wrapper : enchants) {
            if (wrapper.getEnchantment().equals(enchant)) {
                return true;
            }
        }

        return false;
    }

    public static boolean hasEnchantment(ItemStack item, Enchantment enchant, int level) {
        if (!Items.hasEnchantments(item)) {
            return false;
        }

        Set<ItemEnchantmentWrapper> enchants = getEnchantments(item);
        for (ItemEnchantmentWrapper wrapper : enchants) {
            if (wrapper.getEnchantment().equals(enchant) && wrapper.getLevel() == level) {
                return true;
            }
        }
        return false;
    }

    public static List<ItemStack> findSimilarEnchantedItems(ItemStack item, Collection<ItemStack> itemsToCheck) {
        List<ItemStack> items = new ArrayList<>();
        for (ItemStack itemStack : itemsToCheck) {
            if (hasSameEnchantments(item, itemStack)) {
                items.add(itemStack);
            }
        }

        return items;
    }

    public static Set<ItemEnchantmentWrapper> getEnchantments(ItemStack item) {
        Set<ItemEnchantmentWrapper> enchants = Sets.newHashSet();

        if (!hasEnchantments(item)) {
            return enchants;
        }

        for (Entry<Enchantment, Integer> enchant : item.getEnchantments().entrySet()) {
            enchants.add(new ItemEnchantmentWrapper(enchant.getKey(), enchant.getValue(), false, enchant.getKey().isTreasure()));
        }

        return enchants;
    }

    public static void addFlags(ItemStack item, ItemFlag... flag) {
        ItemMeta meta = getMetadata(item);
        meta.addItemFlags(flag);
        setMetadata(item, meta);
    }

    public static void addFlags(ItemStack item, Collection<ItemFlag> flags) {
        ItemMeta meta = getMetadata(item);
        flags.forEach(flag -> meta.addItemFlags(flag));
        setMetadata(item, meta);
    }

    public static ItemStack makeItem(Material material) {
        return new ItemStack(material);
    }

    public static boolean isType(ItemStack itemStack, Material material) {
        return itemStack != null && material == itemStack.getType();
    }

    public static boolean isArmor(ItemStack itemStack) {
        if (itemStack == null) {
            return false;
        }

        return isArmor(itemStack.getType());
    }

    public static boolean isArmor(Material material) {
        if (material == null) {
            return false;
        }
        return armorMaterials.contains(material);
    }

    public static boolean isWeapon(ItemStack itemStack) {
        return WeaponType.isItemWeapon(itemStack);
    }

    public static boolean isWeapon(ItemStack item, WeaponType type) {
        return WeaponType.isItemWeapon(item, type);
    }

    public static boolean isWeapon(Material material) {
        return WeaponType.isMaterialWeapon(material);
    }

    public static boolean isTool(ItemStack item) {
        return isTool(item.getType());
    }

    public static boolean isTool(ItemStack item, ToolType type) {
        return isTool(item.getType(), type);
    }

    public static boolean isTool(Material material, ToolType type) {
        return type.isType(material);
    }

    public static boolean isTool(Material type) {
        return ToolType.isTool(type);
    }

    public static boolean isOre(ItemStack item) {
        return Blocks.isOre(item.getType());
    }

    public static boolean isSmeltableOre(Material type) {
        switch (type) {
            case IRON_ORE:
            case GOLD_ORE:
                return true;
            default:
                return false;
        }
    }

    public static boolean isSmeltableOre(ItemStack item) {
        return isSmeltableOre(item.getType());
    }

    public static Set<ItemStack> getToolSet(ToolType type) {
        Set<ItemStack> items = type.getMaterialTypes().stream()
                .map(itemType -> ItemBuilder.of(itemType).amount(itemType.getMaxStackSize()).item())
                .collect(Collectors.toSet());
        return items;
    }

    public static Set<ItemStack> getToolSet(ToolType type, int stackSize) {
        Set<ItemStack> items = type.getMaterialTypes().stream()
                .map(itemType -> ItemBuilder.of(itemType).amount(stackSize).item())
                .collect(Collectors.toSet());
        return items;
    }

    public static ItemStack makeItem(Material material, int dataVal) {
        return getMaterialData(material, dataVal).toItemStack(1);
    }

    public static ItemStack makeLeatherItem(Material material, String itemName, List<String> itemLore, Map<Enchantment, Integer> enchantments, Color itemColor) {
        ItemStack itemStack = ItemBuilder.of(material).name(itemName).lore(itemLore).item();
        for (Entry<Enchantment, Integer> enchantment : enchantments.entrySet()) {
            addEnchantment(itemStack, enchantment.getKey(), enchantment.getValue(), true);
        }
        return setColor(itemStack, itemColor);
    }

    public static ItemStack makeLeatherItem(Material material, Color leatherColor) {
        ItemStack itemStack = new ItemStack(material);
        return setColor(itemStack, leatherColor);
    }

    public static boolean isPlayerSkull(ItemStack item) {
        if (item.getType() != Material.SKELETON_SKULL) {
            return false;
        }

        if (getDataValue(item) != 3) {
            return false;
        }

        try {
            SkullMeta meta = (SkullMeta) item.getItemMeta();
            return meta.hasOwner();
        } catch (Exception e) {
            YPlugin.getInstance().getLogger().log(Level.SEVERE, "Metadata cast exception", e);
        }

        return true;
    }

    public static ItemStack getSkull(String playerName) {
        ItemStack skullStack = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) skullStack.getItemMeta();
        skullMeta.setOwner(playerName);
        skullStack.setItemMeta(skullMeta);
        return skullStack;
    }

    public static String getFormattedMaterialName(ItemStack itemStack) {
        return getFormattedMaterialName(itemStack.getType());
    }

    public static String getFormattedMaterialName(Material Material) {
        return WordUtils.capitalize(Material.name().toLowerCase().replaceAll("_", " "));
    }

    public static Material unFormatMaterialName(String string) {
        return Material.valueOf(string.toUpperCase().replaceAll(" ", "_"));
    }

    public static Material getMaterialByName(String search) throws InvalidMaterialNameException {
        try {
            Material.valueOf(search.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidMaterialNameException(String.format(Chat.format("&e%s &cis not a valid material name."), search));
        }

        Material material = null;
        try {
            material = Material.valueOf(search.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidMaterialNameException(String.format(Chat.format("&e%s &cis not a valid material name."), search));
        }

        return material;
    }

    public static Dye getDye(DyeColor dyeColor) {
        Dye dye = new Dye();
        dye.setColor(dyeColor);
        return dye;
    }

    public static MaterialData getMaterialDataFromString(String idDatavalue) throws InvalidMaterialNameException {

        String[] materialSplit = null;
        Material material = null;
        if (idDatavalue.contains(":")) {
            materialSplit = idDatavalue.split(":");
            String materialKey = materialSplit[0];
            boolean secondNumeric = StringUtils.isNumeric(materialSplit[1]);

            material = Material.getMaterial(materialKey);

            if (material == null) {
                throw new InvalidMaterialNameException(StringUtil.stripColor(Messages.invalidItem(idDatavalue)));
            }

            return new MaterialData(material, (byte) Integer.parseInt(materialSplit[1]));
        }

        return new MaterialData(material);
    }

    public static MaterialData getMaterialData(Material material, int dataValue) {
        return new MaterialData(material, (byte) dataValue);
    }

    public static ItemStack convertBlockToItem(Block block) {
        return new ItemStack(Blocks.getBlockMaterial(block));
    }

    public static void colouredDurability(ItemStack itemStack, boolean isRed) {
        itemStack.setDurability((short) (isRed ? 1000 : itemStack.getType().getMaxDurability() * 2));
    }

    public static boolean repairItem(ItemStack itemStack) {
        if (itemStack == null || itemStack.getDurability() == 0 || itemStack.getType().isBlock()) {
            return false;
        }
        itemStack.setDurability((short) 0);
        return true;
    }

    public static int repairItems(ItemStack... itemStacks) {
        int repairedItems = 0;
        for (ItemStack itemStack : itemStacks) {
            if (itemStack == null || itemStack.getType() == Material.AIR) {
                continue;
            }
            repairedItems += repairItem(itemStack) ? 1 : 0;
        }
        return repairedItems;
    }

    public static boolean isAir(ItemStack itemStack) {
        return itemStack == null || itemStack.getType() == Material.AIR;
    }

    public static boolean isBook(ItemStack itemStack) {
        return (getMetadata(itemStack) instanceof BookMeta);
    }

    public static ItemStack makeBook(String title, String author, String... pages) {
        return makeBook(title, author, Arrays.asList(pages));
    }

    public static ItemStack makeBook(String title, String author, List<String> pages) {
        ItemStack book = makeItem(Material.WRITTEN_BOOK);
        BookMeta bookMeta = (BookMeta) getMetadata(book);
        bookMeta.setTitle(title);
        bookMeta.setAuthor(author);
        bookMeta.setPages(pages);
        return setMetadata(book, bookMeta);
    }

    public static int getDataValue(ItemStack item) {
        return item.getData().getData();
    }

    public static DyeColor getRandomDyeColor() {
        DyeColor[] dyeColors = DyeColor.values();
        return dyeColors[new Random().nextInt(dyeColors.length)];
    }

    private static List<Color> colors = Lists.newArrayList(Color.AQUA, Color.BLACK, Color.BLUE, Color.FUCHSIA, Color.GRAY, Color.GREEN, Color.LIME, Color.MAROON, Color.NAVY, Color.OLIVE, Color.ORANGE, Color.PURPLE, Color.RED, Color.SILVER, Color.YELLOW, Color.WHITE, Color.TEAL);

    public static Color getRandomColor() {
        return ListUtils.getRandom(colors);
    }

    public static boolean hasFlags(ItemStack item) {
        return getMetadata(item).getItemFlags().size() > 0;
    }

    public static Set<ItemFlag> getFlags(ItemStack item) {
        return getMetadata(item).getItemFlags();
    }

    public static boolean isGlowing(ItemStack item) {
        return Items.hasEnchantment(item, GlowingEnchant.getInstance());
    }

    public static void addGlow(ItemStack item) {
        Items.addEnchantment(item, GlowingEnchant.getInstance(), 1, true);
    }

    public static ItemStack removeGlow(ItemStack item) {
        ItemBuilder clone = ItemBuilder.of(item);

        Map<Enchantment, Integer> enchantments = item.getEnchantments();

        if (enchantments.isEmpty()) {
            return item.clone();
        }

        Map<Enchantment, Integer> newEnchants = new HashMap<>();

        for (Entry<Enchantment, Integer> enchant : enchantments.entrySet()) {
            if (enchant.getKey().getClass().isAssignableFrom(GlowingEnchant.class)) {
                continue;
            }

            newEnchants.put(enchant.getKey(), enchant.getValue());
        }

        return clone.enchantments(newEnchants).item();
    }
}