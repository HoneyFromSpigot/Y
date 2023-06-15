package io.github.thewebcode.yplugin.threading.executors;

import com.google.common.base.Preconditions;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public class BukkitExecutors {
    private BukkitExecutors() {
    }

    public static BukkitScheduledExecutorService newSynchronous(final Plugin plugin) {
        final BukkitScheduler scheduler = getScheduler(plugin);
        Preconditions.checkNotNull(plugin, "plugin cannot be NULL");

        AbstractBukkitService service = new AbstractBukkitService(new PendingTasks(plugin, scheduler)) {
            @Override
            protected BukkitTask getTask(Runnable command) {
                return scheduler.runTask(plugin, command);
            }

            @Override
            protected BukkitTask getLaterTask(Runnable task, long ticks) {
                return scheduler.runTaskLater(plugin, task, ticks);
            }

            @Override
            protected BukkitTask getTimerTask(long ticksInitial, long ticksDelay, Runnable task) {
                return scheduler.runTaskTimer(plugin, task, ticksInitial, ticksDelay);
            }
        };

        PluginDisabledListener.getListener(plugin).addService(service);
        return service;
    }

    public static BukkitScheduledExecutorService newAsynchronous(final Plugin plugin) {
        final BukkitScheduler scheduler = getScheduler(plugin);
        Preconditions.checkNotNull(plugin, "plugin cannot be NULL");

        BukkitScheduledExecutorService service = new AbstractBukkitService(new PendingTasks(plugin, scheduler)) {
            @Override
            protected BukkitTask getTask(Runnable command) {
                return scheduler.runTaskAsynchronously(plugin, command);
            }

            @Override
            protected BukkitTask getLaterTask(Runnable task, long ticks) {
                return scheduler.runTaskLaterAsynchronously(plugin, task, ticks);
            }

            @Override
            protected BukkitTask getTimerTask(long ticksInitial, long ticksDelay, Runnable task) {
                return scheduler.runTaskTimerAsynchronously(plugin, task, ticksInitial, ticksDelay);
            }
        };

        PluginDisabledListener.getListener(plugin).addService(service);
        return service;
    }

    private static BukkitScheduler getScheduler(Plugin plugin) {
        BukkitScheduler scheduler = plugin.getServer().getScheduler();

        if (scheduler != null) {
            return scheduler;
        } else {
            throw new IllegalStateException("Unable to retrieve scheduler.");
        }
    }
}
