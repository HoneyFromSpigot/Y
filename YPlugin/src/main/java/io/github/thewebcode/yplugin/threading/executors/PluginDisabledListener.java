package io.github.thewebcode.yplugin.threading.executors;

import com.google.common.collect.MapMaker;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class PluginDisabledListener implements Listener {
    private static ConcurrentMap<Plugin, PluginDisabledListener> listeners = new MapMaker().weakKeys().makeMap();
    private Set<Future<?>> futures = Collections.newSetFromMap(new WeakHashMap<>());
    private Set<ExecutorService> services = Collections.newSetFromMap(new WeakHashMap<>());
    private Object setLock = new Object();

    private final Plugin plugin;
    private boolean disabled;

    private PluginDisabledListener(Plugin plugin) {
        this.plugin = plugin;
    }

    public static PluginDisabledListener getListener(final Plugin plugin) {
        PluginDisabledListener result = listeners.get(plugin);

        if (result == null) {
            final PluginDisabledListener created = new PluginDisabledListener(plugin);
            result = listeners.putIfAbsent(plugin, created);

            if (result == null) {
                BukkitFutures.registerEventExecutor(plugin, PluginDisableEvent.class, EventPriority.NORMAL, (listener, event) -> {
                    if (event instanceof PluginDisableEvent) {
                        created.onPluginDisabled((PluginDisableEvent) event);
                    }
                });

                result = created;
            }
        }
        return result;
    }

    public void addFuture(final ListenableFuture<?> future) {
        synchronized (setLock) {
            if (disabled) {
                processFuture(future);
            } else {
                futures.add(future);
            }
        }
        Futures.addCallback(future, new FutureCallback<Object>() {
            @Override
            public void onSuccess(Object value) {
                synchronized (setLock) {
                    futures.remove(future);
                }
            }

            @Override
            public void onFailure(Throwable ex) {
                synchronized (setLock) {
                    futures.remove(future);
                }
            }
        }, new Executor() {
            @Override
            public void execute(@NotNull Runnable command) {
                command.run();
            }
        });
    }

    public void addService(ExecutorService service) {
        synchronized (setLock) {
            if (disabled) {
                processService(service);
            } else {
                services.add(service);
            }
        }
    }

    public void onPluginDisabled(PluginDisableEvent e) {
        if (e.getPlugin().equals(plugin)) {
            synchronized (setLock) {
                disabled = true;
                for (Future<?> future : futures) {
                    processFuture(future);
                }
                for (ExecutorService service : services) {
                    processService(service);
                }
            }
        }
    }

    private void processFuture(Future<?> future) {
        if (!future.isDone()) {
            future.cancel(true);
        }
    }

    private void processService(ExecutorService service) {
        service.shutdownNow();
    }
}
