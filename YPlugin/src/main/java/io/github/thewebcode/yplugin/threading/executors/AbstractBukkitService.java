package io.github.thewebcode.yplugin.threading.executors;

import com.google.common.base.Throwables;
import com.google.common.util.concurrent.ListenableScheduledFuture;
import org.bukkit.scheduler.BukkitTask;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

abstract class AbstractBukkitService extends AbstractListeningService {

    private static final long MILLISECONDS_PER_TICK = 50;
    private static final long NANOSECONDS_PER_TICK = 1000000 * MILLISECONDS_PER_TICK;

    private volatile boolean shutdown;
    private PendingTasks tasks;

    public AbstractBukkitService(PendingTasks tasks) {
        this.tasks = tasks;
    }

    @Override
    protected <T> RunnableAbstractFuture<T> newTaskFor(Runnable runnable, T value) {
        return newTaskFor(Executors.callable(runnable, value));
    }

    @Override
    protected <T> RunnableAbstractFuture<T> newTaskFor(final Callable<T> callable) {
        validateState();
        return new CallableTask<>(callable);
    }

    @Override
    public void execute(Runnable command) {
        validateState();

        if (command instanceof RunnableFuture) {
            tasks.add(getTask(command), (Future<?>) command);
        } else {
            submit(command);
        }
    }

    protected abstract BukkitTask getTask(Runnable command);

    protected abstract BukkitTask getLaterTask(Runnable task, long ticks);

    protected abstract BukkitTask getTimerTask(long ticksInitial, long ticksDelay, Runnable task);

    @Override
    public List<Runnable> shutdownNow() {
        shutdown();
        tasks.cancel();
        return Collections.emptyList();
    }

    @Override
    public void shutdown() {
        shutdown = true;
    }

    private void validateState() {
        if (shutdown) {
            throw new RejectedExecutionException("Executor service has shut down. Cannot start new tasks.");
        }
    }

    private long toTicks(long delay, TimeUnit unit) {
        return Math.round(unit.toMillis(delay) / (double) MILLISECONDS_PER_TICK);
    }

    public ListenableScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
        return schedule(Executors.callable(command), delay, unit);
    }

    public <V> ListenableScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
        long ticks = toTicks(delay, unit);
        CallableTask<V> task = new CallableTask<>(callable);
        BukkitTask bukkitTask = getLaterTask(task, ticks);

        tasks.add(bukkitTask, task);
        return task.getScheduledFuture(System.nanoTime() + delay * NANOSECONDS_PER_TICK, 0);
    }

    public ListenableScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay,
                                                            long period, TimeUnit unit) {

        long ticksInitial = toTicks(initialDelay, unit);
        long ticksDelay = toTicks(period, unit);
        CallableTask<?> task = new CallableTask<Object>(Executors.callable(command)) {
            protected void compute() {
                try {
                    compute.call();
                } catch (Exception e) {
                    throw Throwables.propagate(e);
                }
            }
        };
        BukkitTask bukkitTask = getTimerTask(ticksInitial, ticksDelay, task);

        tasks.add(bukkitTask, task);
        return task.getScheduledFuture(
                System.nanoTime() + ticksInitial * NANOSECONDS_PER_TICK,
                ticksDelay * NANOSECONDS_PER_TICK);
    }

    @Deprecated
    public ListenableScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
        return scheduleAtFixedRate(command, initialDelay, delay, unit);
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return tasks.awaitTermination(timeout, unit);
    }

    @Override
    public boolean isShutdown() {
        return shutdown;
    }

    @Override
    public boolean isTerminated() {
        return tasks.isTerminated();
    }
}
