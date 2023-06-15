package io.github.thewebcode.yplugin.nms.minecraft_1_20_R1;

import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.event.StackTraceEvent;
import io.github.thewebcode.yplugin.nms.UnhandledStackTrace;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.joor.Reflect;

public class UnhandledStackTrace_1_20_R1 implements UnhandledStackTrace {
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        try {
            StackTraceEvent.call(e);
        } catch (Throwable th) {
            e.printStackTrace();
        }
    }

    @Override
    public void register() {
        Thread.setDefaultUncaughtExceptionHandler(this);

        try {
            Server server = Bukkit.getServer();
            Object minecraftServer = Reflect.on(Bukkit.getServer()).call("getServer").get();

            Thread serverThread = Reflect.on(minecraftServer).get("serverThread");
            serverThread.setUncaughtExceptionHandler(this);

            Thread primaryThread = Reflect.on(minecraftServer).get("primaryThread");
            primaryThread.setUncaughtExceptionHandler(this);
            Chat.messageConsole("Registered uncaught exception handler for serverThread and PrimaryThread");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
