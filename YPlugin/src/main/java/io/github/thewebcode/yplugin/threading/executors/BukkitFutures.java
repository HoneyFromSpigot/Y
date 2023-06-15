package io.github.thewebcode.yplugin.threading.executors;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import org.bukkit.event.*;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.IllegalPluginAccessException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicBoolean;

public class BukkitFutures {
    private static Listener EMPTY_LISTENER = new Listener() {
    };

    public static <TEvent extends Event> ListenableFuture<TEvent> nextEvent(Plugin plugin, Class<TEvent> eventClass) {
        return BukkitFutures.nextEvent(plugin, eventClass, EventPriority.NORMAL, false);
    }

    public static <TEvent extends Event> ListenableFuture<TEvent> nextEvent(
            Plugin plugin, Class<TEvent> eventClass, EventPriority priority, boolean ignoreCancelled) {
        final HandlerList list = getHandlerList(eventClass);
        final SettableFuture<TEvent> future = SettableFuture.create();

        EventExecutor executor = new EventExecutor() {
            private final AtomicBoolean once = new AtomicBoolean();

            @SuppressWarnings("unchecked")
            @Override
            public void execute(Listener listener, Event event) throws EventException {
                if (!future.isCancelled() && !once.getAndSet(true)) {
                    future.set((TEvent) event);
                }
            }
        };
        RegisteredListener listener = new RegisteredListener(EMPTY_LISTENER, executor, priority, plugin, ignoreCancelled) {
            @Override
            public void callEvent(Event event) throws EventException {
                super.callEvent(event);
                list.unregister(this);
            }
        };
        PluginDisabledListener.getListener(plugin).addFuture(future);
        list.register(listener);
        return future;
    }

    public static void registerEventExecutor(Plugin plugin, Class<? extends Event> eventClass, EventPriority priority, EventExecutor executor) {
        getHandlerList(eventClass).register(
                new RegisteredListener(EMPTY_LISTENER, executor, priority, plugin, false)
        );
    }

    private static HandlerList getHandlerList(Class<? extends Event> clazz) {
        while (clazz.getSuperclass() != null && Event.class.isAssignableFrom(clazz.getSuperclass())) {
            try {
                Method method = clazz.getDeclaredMethod("getHandlerList");
                method.setAccessible(true);
                return (HandlerList) method.invoke(null);
            } catch (NoSuchMethodException e) {
                clazz = clazz.getSuperclass().asSubclass(Event.class);
            } catch (Exception e) {
                throw new IllegalPluginAccessException(e.getMessage());
            }
        }
        throw new IllegalPluginAccessException("Unable to find handler list for event "
                + clazz.getName());
    }
}
