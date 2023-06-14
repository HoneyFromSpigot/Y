package io.github.thewebcode.yplugin.plugin;

public interface YPlugin {
    public void startup();
    public void shutdown();

    public String getVersion();
    public String getAuthor();
    public void initConfig();
}
