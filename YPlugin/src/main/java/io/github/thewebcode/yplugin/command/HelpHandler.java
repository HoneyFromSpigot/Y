package io.github.thewebcode.yplugin.command;

public interface HelpHandler {
    public String[] getHelpMessage(RegisteredCommand command);

    public String getUsage(RegisteredCommand command);
}
