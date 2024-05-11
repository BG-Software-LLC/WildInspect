package com.bgsoftware.wildinspect.coreprotect.lookup;

import java.time.Duration;
import java.util.Date;

public class DataResultLine implements LookupResultLine {

    private final Date date;
    private final String timeSinceAction;
    private final String playerName;
    private final String action;
    private final String blockAction;
    private Duration duration;

    public DataResultLine(Date date, String timeSinceAction, String playerName, String action, String blockAction) {
        this.date = date;
        this.timeSinceAction = timeSinceAction;
        this.playerName = playerName;
        this.action = action;
        this.blockAction = blockAction;
    }

    @Override
    public Type getType() {
        return Type.DATA;
    }

    public Date getDate() {
        return date;
    }

    public String getTimeSinceAction() {
        return timeSinceAction;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getAction() {
        return action;
    }

    public String getBlockAction() {
        return blockAction;
    }

}
