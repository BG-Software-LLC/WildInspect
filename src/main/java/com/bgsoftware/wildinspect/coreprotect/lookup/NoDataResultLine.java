package com.bgsoftware.wildinspect.coreprotect.lookup;

public class NoDataResultLine implements LookupResultLine {

    private final InteractionType interactionType;

    public NoDataResultLine(InteractionType interactionType) {
        this.interactionType = interactionType;
    }

    public NoDataResultLine(InteractionType interactionType, String unused) {
        this.interactionType = interactionType;
    }

    @Override
    public Type getType() {
        return Type.NO_DATA;
    }

    public InteractionType getInteractionType() {
        return this.interactionType;
    }

    public enum InteractionType {

        PLAYER_INTERACTIONS,
        BLOCK_DATA,
        CONTAINER_TRANSACTIONS

    }

}
