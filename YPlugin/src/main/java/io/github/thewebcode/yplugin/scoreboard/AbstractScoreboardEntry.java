package io.github.thewebcode.yplugin.scoreboard;

import io.github.thewebcode.yplugin.utilities.StringUtil;


public abstract class AbstractScoreboardEntry implements ScoreboardEntry {
    private String oldText = "";
    private String text = "";

    private int oldScore = 0;
    private int score = 0;

    private boolean change = false;

    public AbstractScoreboardEntry(String text, int score) {
        this.text = StringUtil.colorize(text);
        this.score = score;
    }

    public AbstractScoreboardEntry() {

    }

    @Override
    public String getValue() {
        return text;
    }

    @Override
    public String getPreviousValue() {
        return oldText;
    }

    public void setValue(String text) {
        this.oldText = this.text;
        this.text = StringUtil.colorize(text);
        change = true;
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public int getPreviousScore() {
        return oldScore;
    }

    @Override
    public void setScore(int score) {
        this.score = score;
        change = true;
    }

    @Override
    public boolean hasChanged() {
        return change;
    }

    @Override
    public void setChanged(boolean changed) {
        this.change = changed;
    }
}
