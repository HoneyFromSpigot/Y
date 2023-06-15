package io.github.thewebcode.yplugin.warp;

import com.google.common.collect.Lists;
import io.github.thewebcode.yplugin.YPlugin;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.*;

public class Warps {
    public static int pages = 0;
    private static boolean initialized = false;
    private static boolean updated = false;
    private static Map<String, Warp> warps = new HashMap<>();

    private static Map<Integer, List<Warp>> warpPages = new HashMap<>();

    public static boolean isWarp(String name) {
        for (String warpName : warps.keySet()) {
            if (name.equalsIgnoreCase(warpName)) {
                return true;
            }
        }
        return false;
    }

    public static void addWarp(Warp warp) {
        warps.put(warp.getName(), warp);
        setUpdated(true);
        YPlugin.getInstance().debug("Added warp " + warp.getName());
    }

    private static void initWarpPages() {
        List<List<Warp>> warpLists = Lists.partition(new ArrayList<>(warps.values()), 52);
        int i = 1;
        for (List<Warp> pages : warpLists) {
            warpPages.put(i, pages);
            i += 1;
        }
    }

    public static List<Warp> getWarpsPage(int page) {
        if (isUpdated()) {
            initWarpPages();
        }
        return warpPages.get(page);
    }

    public static int getWarpPagesCount() {
        return warpPages.size();
    }

    public static void addWarp(Warp warp, boolean saveFile) {
        addWarp(warp);
        if (saveFile) {
            saveWarp(warp);
        }
    }

    public static Warp getWarp(String warpName) {
        for (Warp warp : warps.values()) {
            if (warp.getName().equalsIgnoreCase(warpName)) {
                return warp;
            }
        }
        return null;
    }

    public static Set<String> getWarpNames() {
        return warps.keySet();
    }

    public static void loadWarps() {
        YPlugin.getInstance().debug("Loading warps");
        Collection<File> warpFiles = FileUtils.listFiles(new File(YPlugin.WARP_DATA_FOLDER), null, false);
        for (File file : warpFiles) {
            try {
                Warp warp = new Warp(file);
                warp.load();
                addWarp(warp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        YPlugin.getInstance().debug("Warps Loaded, initializing pages!");
        initWarpPages();
    }

    public static void saveWarps() {
        warps.values().forEach(Warps::saveWarp);
    }

    public static void saveWarp(Warp warp) {
        File warpFile = new File(YPlugin.WARP_DATA_FOLDER + warp.getName() + ".xml");
        try {
            warp.save(warpFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Warp> getWarps() {
        return Lists.newArrayList(warps.values());
    }

    public static int getWarpCount() {
        return warps.size();
    }

    public static boolean isUpdated() {
        return updated;
    }

    public static void setUpdated(boolean updated) {
        Warps.updated = updated;
    }
}
