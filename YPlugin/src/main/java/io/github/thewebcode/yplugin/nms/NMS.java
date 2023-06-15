package io.github.thewebcode.yplugin.nms;


import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.nms.minecraft_1_20_R1.ActionMessageHandler_1_20_R1;
import io.github.thewebcode.yplugin.nms.minecraft_1_20_R1.ParticleEffectsHandler_1_20_R1;
import io.github.thewebcode.yplugin.nms.minecraft_1_20_R1.UnhandledStackTrace_1_20_R1;
import io.github.thewebcode.yplugin.nms.nonbreaking.NonBreakingForceRespawnHandler;
import io.github.thewebcode.yplugin.nms.nonbreaking.NonBreakingInventoryHandler;
import io.github.thewebcode.yplugin.nms.nonbreaking.NonBreakingTitleHandler;
import io.github.thewebcode.yplugin.plugin.Plugins;

public class NMS {

    private static UnhandledStackTrace stackTraceHandler = null;

    private static ActionMessageHandler actionMessageHandler = null;

    private static AbstractTitle.TitleHandler titleHandler = null;

    private static InventoryHandler inventoryHandler = null;

    private static ForceRespawnHandler forceRespawnHandler = null;

    private static ParticleEffectsHandler particleEffectsHandler = null;

    private static boolean initialized = false;

    public static void init() {
        if (initialized) {
            throw new IllegalAccessError("Unable to re-initialize NMS Handler.");
        }

        Chat.debug("NMS Version is: '" + Plugins.getNmsVersion() + "'");

        switch (Plugins.getNmsVersion()) {
            case "v1_20_R1":
                actionMessageHandler = new ActionMessageHandler_1_20_R1();
                titleHandler = new NonBreakingTitleHandler();
                forceRespawnHandler = new NonBreakingForceRespawnHandler();
                particleEffectsHandler = new ParticleEffectsHandler_1_20_R1();
                stackTraceHandler = new UnhandledStackTrace_1_20_R1();
                break;
            default:
                throw new IllegalStateException("Unsupported NMS Version: " + Plugins.getNmsVersion());
        }

        if (stackTraceHandler != null) {
            stackTraceHandler.register();
        }

        inventoryHandler = new NonBreakingInventoryHandler();

        initialized = true;
    }

    public static UnhandledStackTrace getStackTraceHandler() {
        return stackTraceHandler;
    }

    public static ActionMessageHandler getActionMessageHandler() {
        return actionMessageHandler;
    }

    public static AbstractTitle.TitleHandler getTitleHandler() {
        return titleHandler;
    }

    public static InventoryHandler getInventoryHandler() {
        return inventoryHandler;
    }

    public static ForceRespawnHandler getForceRespawnHandler() {
        return forceRespawnHandler;
    }

    public static ParticleEffectsHandler getParticleEffectsHandler() {
        return particleEffectsHandler;
    }

}
