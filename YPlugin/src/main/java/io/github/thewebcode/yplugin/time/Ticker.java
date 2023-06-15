package io.github.thewebcode.yplugin.time;

public interface Ticker {

    public int allowAmount();

    public int getTickCount();

    public void tick();

    public void reset();

    public boolean allow();
}
