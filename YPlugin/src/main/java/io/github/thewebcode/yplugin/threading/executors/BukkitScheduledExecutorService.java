package io.github.thewebcode.yplugin.threading.executors;

import com.google.common.util.concurrent.ListenableScheduledFuture;
import com.google.common.util.concurrent.ListeningScheduledExecutorService;

import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public interface BukkitScheduledExecutorService extends ListeningScheduledExecutorService {

    public ListenableScheduledFuture<?> schedule(
            Runnable command, long delay, TimeUnit unit);

    @Override
    public <V> ListenableScheduledFuture<V> schedule(
            Callable<V> callable, long delay, TimeUnit unit);

    @Override
    public ListenableScheduledFuture<?> scheduleAtFixedRate(
            Runnable command, long initialDelay, long period, TimeUnit unit);

    @Override
    @Deprecated
    public ListenableScheduledFuture<?> scheduleWithFixedDelay(
            Runnable command, long initialDelay, long delay, TimeUnit unit);
}
