package io.github.thewebcode.yplugin.threading.executors;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class PendingTasks {
    private interface CancelableFuture {
        public void cancel();

        public boolean isTaskCancelled();
    }

    private Set<CancelableFuture> pending = new HashSet<>();
    private final Object pendingLock = new Object();

    private final Plugin plugin;
    private final BukkitScheduler scheduler;
    private BukkitTask cancellationTask;

    public PendingTasks(Plugin plugin, BukkitScheduler scheduler) {
        this.plugin = plugin;
        this.scheduler = scheduler;
    }

    public void add(final BukkitTask task, final Future<?> future) {
        add(new CancelableFuture() {
            @Override
            public boolean isTaskCancelled() {
                if (future.isDone()) {
                    return future.isCancelled();
                }

                return !(scheduler.isCurrentlyRunning(task.getTaskId()) ||
                        scheduler.isQueued(task.getTaskId()));
            }

            @Override
            public void cancel() {
                task.cancel();
                future.cancel(true);
            }
        });
    }

    private CancelableFuture add(CancelableFuture task) {
        synchronized (pendingLock) {
            pending.add(task);
            pendingLock.notifyAll();
            beginCancellationTask();
            return task;
        }
    }

    private void beginCancellationTask() {
        if (cancellationTask == null) {
            cancellationTask = scheduler.runTaskTimer(plugin, () -> {
                synchronized (pendingLock) {
                    boolean changed = false;

                    for (Iterator<CancelableFuture> it = pending.iterator(); it.hasNext(); ) {
                        CancelableFuture future = it.next();
                        if (future.isTaskCancelled()) {
                            future.cancel();
                            it.remove();
                            changed = true;
                        }
                    }
                    if (changed) {
                        pendingLock.notifyAll();
                    }
                }
                if (isTerminated()) {
                    cancellationTask.cancel();
                    cancellationTask = null;
                }
            }, 1, 1);
        }
    }

    public void cancel() {
        for (CancelableFuture task : pending) {
            task.cancel();
        }
    }

    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        long expire = System.nanoTime() + unit.toNanos(timeout);

        synchronized (pendingLock) {
            while (!isTerminated()) {
                if (expire < System.nanoTime()) {
                    return false;
                }
                unit.timedWait(pendingLock, timeout);
            }
        }
        return false;
    }

    public boolean isTerminated() {
        return pending.isEmpty();
    }
}
