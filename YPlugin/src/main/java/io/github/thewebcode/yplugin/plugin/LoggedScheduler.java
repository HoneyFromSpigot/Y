package io.github.thewebcode.yplugin.plugin;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public abstract class LoggedScheduler implements BukkitScheduler {
    private class TaskedRunnable implements Runnable{
        private int taskID = -1;
        private Runnable delegate;

        public TaskedRunnable(Runnable runnable){
            this.delegate = runnable;
        }

        @Override
        public void run() {
            try{
                delegate.run();
            } catch (Throwable e){
                customHandler(taskID, e);
            }
        }

        public int getTaskID() {
            return taskID;
        }

        public void setTaskID(int taskID) {
            this.taskID = taskID;
        }
    }

    private BukkitScheduler delegate;

    public LoggedScheduler(Plugin owner){
        this(owner.getServer().getScheduler());
    }

    public LoggedScheduler(BukkitScheduler delegate){
        this.delegate = delegate;
    }

    protected abstract void customHandler(int taskID, Throwable e);

    @Override
    public BukkitTask runTask(Plugin plugin, Runnable runnable) throws IllegalArgumentException, IllegalStateException {
        TaskedRunnable thread = new TaskedRunnable(runnable);
        return delegate.runTask(plugin, thread);
    }

    @Override
    public BukkitTask runTask(Plugin plugin, BukkitRunnable bukkitRunnable) throws IllegalArgumentException{
        TaskedRunnable thread = new TaskedRunnable(bukkitRunnable);
        return delegate.runTask(plugin, thread);
    }

    @Override
    public BukkitTask runTaskAsynchronously(Plugin plugin, Runnable runnable) throws IllegalArgumentException {
        TaskedRunnable thread = new TaskedRunnable(runnable);
        return delegate.runTaskAsynchronously(plugin, thread);
    }

    @Override
    public BukkitTask runTaskAsynchronously(Plugin plugin, BukkitRunnable bukkitRunnable) throws IllegalArgumentException {
        TaskedRunnable thread = new TaskedRunnable(bukkitRunnable);
        return delegate.runTaskAsynchronously(plugin, thread);
    }

    @Override
    public BukkitTask runTaskLater(Plugin plugin, Runnable runnable, long delayInTicks) throws IllegalArgumentException {
        TaskedRunnable thread = new TaskedRunnable(runnable);
        return delegate.runTaskLater(plugin, thread, delayInTicks);
    }

    @Override
    public BukkitTask runTaskLater(Plugin plugin, BukkitRunnable bukkitRunnable, long l) throws IllegalArgumentException {
        TaskedRunnable thread = new TaskedRunnable(bukkitRunnable);
        return delegate.runTaskLater(plugin, thread, l);
    }

    @Override
    public BukkitTask runTaskLaterAsynchronously(Plugin plugin, Runnable runnable, long l) throws IllegalArgumentException {
        TaskedRunnable thread = new TaskedRunnable(runnable);
        return delegate.runTaskLaterAsynchronously(plugin, thread, l);
    }

    @Override
    public BukkitTask runTaskLaterAsynchronously(Plugin plugin, BukkitRunnable bukkitRunnable, long l) throws IllegalArgumentException {
        TaskedRunnable thread = new TaskedRunnable(bukkitRunnable);
        return delegate.runTaskLaterAsynchronously(plugin, thread, l);
    }

    @Override
    public BukkitTask runTaskTimer(Plugin plugin, Runnable runnable, long delayInTicks, long repeatTicks) throws IllegalArgumentException {
        TaskedRunnable thread = new TaskedRunnable(runnable);
        return delegate.runTaskTimer(plugin, thread, delayInTicks, repeatTicks);
    }

    @Override
    public BukkitTask runTaskTimer(Plugin plugin, BukkitRunnable bukkitRunnable, long l, long l1) throws IllegalArgumentException {
        TaskedRunnable thread = new TaskedRunnable(bukkitRunnable);
        return delegate.runTaskTimer(plugin, thread, l, l1);
    }

    @Override
    public BukkitTask runTaskTimerAsynchronously(Plugin plugin, Runnable runnable, long delay, long repeat) throws IllegalArgumentException {
        TaskedRunnable thread = new TaskedRunnable(runnable);
        return delegate.runTaskTimerAsynchronously(plugin, thread, delay, repeat);
    }

    @Override
    public BukkitTask runTaskTimerAsynchronously(Plugin plugin, BukkitRunnable bukkitRunnable, long l, long l1) throws IllegalArgumentException {
        TaskedRunnable thread = new TaskedRunnable(bukkitRunnable);
        return delegate.runTaskTimerAsynchronously(plugin, thread, l, l1);
    }

    @Override
    public <T> Future<T> callSyncMethod(Plugin plugin, Callable<T> task) {
        return delegate.callSyncMethod(plugin, task);
    }

    @Override
    public void cancelTask(int taskId) throws IllegalArgumentException, IllegalStateException {
        delegate.cancelTask(taskId);
    }

    @Override
    public void cancelTasks(Plugin plugin) {
        delegate.cancelTasks(plugin);
    }

    @Override
    public List<BukkitTask> getPendingTasks() {
        return delegate.getPendingTasks();
    }

    @Override
    public boolean isCurrentlyRunning(int taskID){
        return delegate.isCurrentlyRunning(taskID);
    }

    @Override
    public boolean isQueued(int taskID){
        return delegate.isQueued(taskID);
    }

    @Override
    public int scheduleAsyncDelayedTask(Plugin plugin, Runnable task){
        TaskedRunnable wrapped = new TaskedRunnable(task);

        wrapped.setTaskID(delegate.scheduleAsyncDelayedTask(plugin, wrapped));
        return wrapped.getTaskID();
    }

    @Override
    public int scheduleAsyncDelayedTask(Plugin plugin, Runnable task, long delay){
        TaskedRunnable wrapped = new TaskedRunnable(task);

        wrapped.setTaskID(delegate.scheduleAsyncDelayedTask(plugin, wrapped, delay));
        return wrapped.getTaskID();
    }

    @Override
    public int scheduleAsyncRepeatingTask(Plugin plugin, Runnable task, long delay, long period){
        TaskedRunnable wrapped = new TaskedRunnable(task);

        wrapped.setTaskID(delegate.scheduleAsyncRepeatingTask(plugin, wrapped, delay, period));
        return wrapped.getTaskID();
    }

    @Override
    public int scheduleSyncDelayedTask(Plugin plugin, Runnable task){
        TaskedRunnable wrapped = new TaskedRunnable(task);

        wrapped.setTaskID(delegate.scheduleSyncDelayedTask(plugin, wrapped));
        return wrapped.getTaskID();
    }

    @Override
    public int scheduleSyncDelayedTask(Plugin plugin, BukkitRunnable task){
        TaskedRunnable wrapped = new TaskedRunnable(task);

        wrapped.setTaskID(delegate.scheduleSyncDelayedTask(plugin, wrapped));
        return wrapped.getTaskID();
    }

    @Override
    public int scheduleSyncDelayedTask(Plugin plugin, Runnable task, long delay){
        TaskedRunnable wrapped = new TaskedRunnable(task);

        wrapped.setTaskID(delegate.scheduleSyncDelayedTask(plugin, wrapped, delay));
        return wrapped.getTaskID();
    }

    @Override
    public int scheduleSyncDelayedTask(Plugin plugin, BukkitRunnable task, long delay){
        TaskedRunnable wrapped = new TaskedRunnable(task);

        wrapped.setTaskID(delegate.scheduleSyncDelayedTask(plugin, wrapped, delay));
        return wrapped.getTaskID();
    }

    @Override
    public int scheduleSyncRepeatingTask(Plugin plugin, Runnable task, long delay, long period){
        TaskedRunnable wrapped = new TaskedRunnable(task);

        wrapped.setTaskID(delegate.scheduleSyncRepeatingTask(plugin, wrapped, delay, period));
        return wrapped.getTaskID();
    }

    @Override
    public int scheduleSyncRepeatingTask(Plugin plugin, BukkitRunnable task, long delay, long period){
        TaskedRunnable wrapped = new TaskedRunnable(task);

        wrapped.setTaskID(delegate.scheduleSyncRepeatingTask(plugin, wrapped, delay, period));
        return wrapped.getTaskID();
    }

    public void cancelAllTasks(Plugin plugin){
        delegate.cancelTasks(plugin);
    }

}
