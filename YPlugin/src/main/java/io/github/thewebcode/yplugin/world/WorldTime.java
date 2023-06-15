package io.github.thewebcode.yplugin.world;

public enum WorldTime {
    DAWN(0),
    DAY(1000),
    NIGHT(12000);

    private long time;

    WorldTime(long time) {
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    public static WorldTime getWorldTime(String time) {
        return WorldTime.valueOf(time.toUpperCase());
    }
}
